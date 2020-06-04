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
import org.gradle.api.tasks.*
import org.gradle.kotlin.dsl.*
import java.io.File
import java.nio.charset.Charset

/**
 * Created by JunHyung Lim on 2020-04-28
 */
@Suppress("UnstableApiUsage")
open class GenerateYaml : DefaultTask() {
    init {
        group = "spigradle"
        description = "Generate the yaml file"
    }

    @Input
    val properties: MapProperty<String, Any> = project.objects.mapProperty()

    @Input
    val encoding: Property<String> = project.objects.property<String>().convention("UTF-8")

    @Input
    val yamlOptions: MapProperty<String, Boolean> = project.objects.mapProperty()

    @OutputFiles
    val outputFiles: ConfigurableFileCollection = project.objects.fileCollection()

    fun serializeToProperties(provider: Provider<Map<String, Any>>) = properties.set(provider)

    fun serializeToProperties(any: Any) = serializeToProperties(project.provider {
        Jackson.MAPPER.convertValue<Map<String, Any>>(any).toMutableMap()
    })

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
                logger.warn("The given name '$featureEnumKey' on yamlOptions is invalid key.")
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
    val description = extensions.create<T>(extensionName, this).apply {
        group = taskGroupName
    }
    val detectionTask = SubclassDetection.register(this, detectionTaskName, pluginSuperClass).applyToConfigure {
        group = taskGroupName
    }
    val generateTask = registerYamlGenTask(yamlTaskName, extensionName, descFileName, description).applyToConfigure {
        group = taskGroupName
    }
    val classes: Task by tasks
    description.init(project)
    // classes -> detectionTask -> generateTask
    classes.finalizedBy(detectionTask)
    detectionTask.configure { finalizedBy(generateTask) }
}

internal fun Project.registerYamlGenTask(taskName: String, extensionName: String, fileName: String, data: MainProvider): TaskProvider<GenerateYaml> {
    return project.tasks.register(taskName, GenerateYaml::class) {
        val sourceSets = project.withConvention(JavaPluginConvention::class) { sourceSets }
        listOf("main", "test").mapNotNull {
            sourceSets[it].output.resourcesDir
        }.forEach { resourceDir ->
            outputFiles.from(File(resourceDir, fileName))
        }
        serializeToProperties(provider {
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