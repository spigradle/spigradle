/*
 * Copyright (c) 2020 Spigradle contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kr.entree.spigradle.module.common

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator
import com.fasterxml.jackson.module.kotlin.convertValue
import kr.entree.spigradle.internal.*
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFiles
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.*
import java.io.File
import java.nio.charset.Charset
import kr.entree.spigradle.annotations.processor.PluginAnnotationProcessor.PLUGIN_APT_DEFAULT_PATH

/**
 * Generates a YAML into the given files.
 *
 * Groovy Example:
 *
 * ```groovy
 * import kr.entree.spigradle.module.common.YamlGenerate
 *
 * task generateYaml(type: YamlGenerate) {
 *   properties.put('someProperty', 'AnyTypeOfValue')
 *   encoding = 'UTF-16'
 *   yamlOptions.put('WRITE_DOC_START_MARKER', true)
 *   outputFiles.from file('result.yml')
 * }
 * ```
 *
 * Kotlin Example:
 *
 * ```kotlin
 * import kr.entree.spigradle.module.common.YamlGenerate
 *
 * tasks {
 *   val generateYaml by registering(YamlGenerate) {
 *     properties.put("someProperty", "AnyTypeOfValue")
 *     encoding.set("UTF-16")
 *     yamlOptions.put("WRITE_DOC_START_MARKER", true)
 *     outputFiles.from(file("result.yml"))
 *   }
 * }
 * ```
 *
 * @since 1.3.0
 */
@Suppress("UnstableApiUsage")
open class YamlGenerate : DefaultTask() {
    init {
        group = "spigradle"
        description = "Generate the yaml file"
    }

    /**
     * The property map of yaml.
     */
    @Input
    val properties: MapProperty<String, Any> = project.objects.mapProperty()

    /**
     * The encoding of the file.
     */
    @Input
    val encoding: Property<String> = project.objects.property<String>().convention("UTF-8")

    /**
     * The yaml options. the key is Enum#name() of [com.fasterxml.jackson.dataformat.yaml.YAMLGenerator.Feature].
     */
    @Input
    val yamlOptions: MapProperty<String, Boolean> = project.objects.mapProperty()

    /**
     * The files that will be output.
     */
    @OutputFiles
    val outputFiles: ConfigurableFileCollection = project.objects.fileCollection()

    /**
     * Sets the value that will be serialized.
     *
     * @param provider The lazy provider of the value, pass to using `[org.gradle.api.Project.provider] { value }`
     */
    fun serialize(provider: Provider<Any>) = properties.set(provider.map {
        Jackson.JSON.convertValue<Map<String, Any>>(it)
    })

    /**
     * Sets the value that will be serialized. it simply calls `serialize([org.gradle.api.Project.provider] { [any] }`
     *
     * @param any The value that will be serialized.
     */
    fun serialize(any: Any) = serialize(project.provider { any })

    @TaskAction
    fun generate() {
        val yaml = YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
                .enable(YAMLGenerator.Feature.MINIMIZE_QUOTES)
                .enable(YAMLGenerator.Feature.INDENT_ARRAYS)
                .applyUserOptions()
        val mapper = ObjectMapper(yaml)
        val encoding = Charset.forName(encoding.get())
        val contents = properties.orNull?.run { mapper.writeValueAsString(this) } ?: ""
        outputFiles.forEach { file ->
            file.bufferedWriter(encoding).use {
                it.write(contents)
            }
        }
    }

    private fun YAMLFactory.applyUserOptions() = apply {
        yamlOptions.orNull?.forEach { (featureEnumKey, turnOn) ->
            runCatching {
                YAMLGenerator.Feature.valueOf(featureEnumKey.toUpperCase())
            }.onSuccess {
                if (turnOn) enable(it)
                else disable(it)
            }.onFailure {
                logger.warn("The given name '$featureEnumKey' on yamlOptions is invalid key.", it)
            }
        }
    }
}

internal inline fun <reified T : StandardDescription> Project.registerDescGenTask(
        extensionName: String,
        yamlTaskName: String,
        detectionTaskName: String,
        descFileName: String,
        pluginSuperClass: String,
        taskGroupName: String = extensionName
) {
    val description = extensions.create<T>(extensionName, this)
    val detectionTask = SubclassDetection.register(this, detectionTaskName, pluginSuperClass).applyToConfigure {
        group = taskGroupName
    }
    val generationTask = registerYamlGenTask(yamlTaskName, extensionName, descFileName, description).applyToConfigure {
        group = taskGroupName
    }
    val classes: Task by tasks
    project.afterEvaluate {
        description.init(this)
    }
    // classes -> detectionTask -> generateTask
    generationTask.configure { dependsOn(detectionTask) }
    classes.finalizedBy(generationTask)
}

internal fun Project.registerYamlGenTask(taskName: String, extensionName: String, fileName: String, data: MainProvider): TaskProvider<YamlGenerate> {
    return project.tasks.register(taskName, YamlGenerate::class) {
        val sourceSets = project.withConvention(JavaPluginConvention::class) { sourceSets }
        listOf("main", "test").mapNotNull {
            sourceSets[it].output.resourcesDir
        }.forEach { resourceDir ->
            outputFiles.from(File(resourceDir, fileName))
        }
        outputFiles.from(File(temporaryDir, fileName))
        serialize(provider {
            data.apply {
                if (main == null) {
                    main = runCatching {
                        File(project.buildDir, PLUGIN_APT_DEFAULT_PATH).readText()
                    }.getOrNull()
                }
            }
        })
        doFirst {
            notNull(data.main) { Messages.noMainFound(extensionName, taskName) }
        }
    }
}