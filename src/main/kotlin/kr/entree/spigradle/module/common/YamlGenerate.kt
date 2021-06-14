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
import kr.entree.spigradle.annotations.PluginType
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

// TODO: Too complex! Should whole refactor in 3.0
internal inline fun <reified T : StandardDescription> Project.registerDescGenTask(
    type: PluginConvention, crossinline mapDesc: (T) -> T = { it }
) {
    val detectResultFile = getPluginMainPathFile(type.mainType)
    val generalResultFile = getPluginMainPathFile(PluginType.GENERAL)
    val description = extensions.create<T>(type.descExtension, this)
    val detectionTask = SubclassDetection.register(this, type.mainDetectTask, type.mainType).applyToConfigure {
        group = type.taskGroup
        superClassName.set(type.mainSuperClass)
        outputFile.set(detectResultFile)
    }
    val generationTask = registerYamlGenTask(type).applyToConfigure {
        inputs.files(detectResultFile, generalResultFile)
        group = type.taskGroup
        serialize(provider {
            mapDesc(description.apply {
                main = main ?: runCatching {
                    detectResultFile.readText()
                }.getOrNull() ?: runCatching {
                    generalResultFile.readText()
                }.getOrNull()
            })
        })
        doFirst {
            notNull(description.main) {
                Messages.noMainFound(type.descExtension, type.descGenTask)
            }
        }
    }
    val classes: Task by tasks
    project.afterEvaluate {
        description.setDefault(this)
    }
    /*
    NOTE: Task ordering part
    https://docs.gradle.org/current/userguide/java_plugin.html

    compileJava       dependsOn: all tasks which contribute to the compilation classpath
    processResources
    *classes          dependsOn: compileJava, processResources
    jar               dependsOn: classes
    *assemble         dependsOn: jar
    *build            dependsOn: assemble

    detectionTask     dependsOn: classes
    generateTask      dependsOn: detectionTask
    *assemble         dependsOn: +generateTask

    Our generate task is part of compilation, thus depends by `classes` which describes compilation.
    Expected ordering: compileJava, ... -> detectionTask -> generateTask -> classes
     */
    generationTask.configure { dependsOn(detectionTask) }
    classes.dependsOn(generationTask)
}

internal fun Project.findResourceDirs(fileName: String): List<File> {
    val sourceSets = project.withConvention(JavaPluginConvention::class) { sourceSets }
    return listOf("main", "test").mapNotNull {
        sourceSets[it].output.resourcesDir
    }.map {
        File(it, fileName)
    }
}

internal fun Project.registerYamlGenTask(taskName: String, fileName: String): TaskProvider<YamlGenerate> {
    return project.tasks.register(taskName, YamlGenerate::class) {
        outputFiles.from(temporaryDir.resolve(fileName))
        outputFiles.from(findResourceDirs(fileName))
    }
}

internal fun Project.registerYamlGenTask(type: PluginConvention): TaskProvider<YamlGenerate> =
    registerYamlGenTask(type.descGenTask, type.descFile)