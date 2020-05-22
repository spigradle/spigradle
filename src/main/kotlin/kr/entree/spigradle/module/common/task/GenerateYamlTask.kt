package kr.entree.spigradle.module.common.task

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator
import com.fasterxml.jackson.module.kotlin.convertValue
import kr.entree.spigradle.internal.Jackson
import kr.entree.spigradle.internal.MainProvider
import kr.entree.spigradle.internal.Messages
import kr.entree.spigradle.internal.PLUGIN_APT_DEFAULT_PATH
import notNull
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.withConvention
import java.io.File
import java.nio.charset.Charset

/**
 * Created by JunHyung Lim on 2020-04-28
 */
open class GenerateYamlTask : DefaultTask() {
    @get:Input
    var properties = mutableMapOf<String, Any>()

    @get:Input
    var encoding: String = "UTF-8"

    @get:OutputFile
    var outputFile: File = File(temporaryDir, "plugin.yml")

    @get:Input
    val yamlOptions = mutableMapOf<String, Boolean>()

    init {
        group = "Spigradle"
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
            }
        }
        outputFile.bufferedWriter(Charset.forName(encoding)).use {
            ObjectMapper(yaml).writeValue(it, properties)
        }
    }

    companion object {
        fun create(project: Project, taskName: String, extensionName: String, data: MainProvider): GenerateYamlTask {
            val sourceSets = project.withConvention(JavaPluginConvention::class) { sourceSets }
            return project.tasks.create(taskName, GenerateYamlTask::class) {
                sourceSets["main"].output.resourcesDir?.let { resourceDir ->
                    project.copy {
                        from(outputFile)
                        into(resourceDir)
                    }
                }
                doFirst {
                    if (data.main == null) {
                        data.main = runCatching {
                            File(project.buildDir, PLUGIN_APT_DEFAULT_PATH).readText()
                        }.getOrNull()
                    }
                    setToOptionMap(data)
                    notNull(data.main) { Messages.noMainFound(extensionName, taskName) }
                }
            }
        }
    }
}