package kr.entree.spigradle.module.common

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator
import com.fasterxml.jackson.module.kotlin.convertValue
import kr.entree.spigradle.internal.*
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.*
import java.io.File
import java.nio.charset.Charset

/**
 * Created by JunHyung Lim on 2020-04-28
 */
internal inline fun <reified T : StandardDescription> Project.setupDescGenTask(
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
    val detectionTask = SubclassDetection.create(this, detectionTaskName, pluginSuperClass).apply {
        group = taskGroupName
    }
    val generateTask = GenerateYaml.create(this, yamlTaskName, extensionName, descFileName, description).apply {
        group = taskGroupName
    }
    val classes: Task by tasks
    description.init(project)
    // classes -> detectionTask -> generateTask
    classes.finalizedBy(detectionTask)
    detectionTask.finalizedBy(generateTask)
}

open class GenerateYaml : DefaultTask() {
    @get:Input
    var properties = mutableMapOf<String, Any>()

    @get:Input
    var encoding: String = "UTF-8"

    @get:OutputFile
    var outputFile: File = File(temporaryDir, "plugin.yml")

    @get:Input
    val yamlOptions = mutableMapOf<String, Boolean>()

    init {
        group = "spigradle"
        description = "Generate yaml file"
    }

    fun setToOptionMap(any: Any) {
        properties.putAll(Jackson.MAPPER.convertValue<Map<String, Any>>(any).toMutableMap())
    }

    @TaskAction
    fun generate() {
        val yaml = YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
                .enable(YAMLGenerator.Feature.MINIMIZE_QUOTES)
                .enable(YAMLGenerator.Feature.INDENT_ARRAYS)
        yamlOptions.forEach { (featureEnumKey, turnOn) ->
            runCatching {
                YAMLGenerator.Feature.valueOf(featureEnumKey.toUpperCase())
            }.onSuccess {
                if (turnOn) yaml.enable(it)
                else yaml.disable(it)
            }.onFailure {
                logger.warn("The given name '$featureEnumKey' on yamlOptions is invalid key.")
            }
        }
        outputFile.bufferedWriter(Charset.forName(encoding)).use {
            ObjectMapper(yaml).writeValue(it, properties)
        }
    }

    companion object {
        internal fun create(project: Project, taskName: String, extensionName: String, fileName: String, data: MainProvider): GenerateYaml {
            val sourceSets = project.withConvention(JavaPluginConvention::class) { sourceSets }
            return project.tasks.create(taskName, GenerateYaml::class) {
                outputFile = File(temporaryDir, fileName)
                doFirst {
                    if (data.main == null) {
                        data.main = runCatching {
                            File(project.buildDir, PLUGIN_APT_DEFAULT_PATH).readText()
                        }.getOrNull()
                    }
                    setToOptionMap(data)
                    notNull(data.main) { Messages.noMainFound(extensionName, taskName) }
                }
                doLast {
                    val resourceDir = sourceSets["main"].output.resourcesDir ?: return@doLast
                    project.copy {
                        from(outputFile)
                        into(resourceDir)
                    }
                }
            }
        }
    }
}