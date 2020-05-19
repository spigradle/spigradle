package kr.entree.spigradle.module.common.task

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator
import com.fasterxml.jackson.module.kotlin.convertValue
import kr.entree.spigradle.internal.Jackson
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.nio.charset.Charset

/**
 * Created by JunHyung Lim on 2020-04-28
 */
open class GenerateYamlTask : DefaultTask() {
    @get:Input
    var options = mutableMapOf<String, Any>()

    @get:Input
    var encoding: String = "UTF-8"

    @get:OutputFile
    var file: File = File(temporaryDir, "plugin.yml")

    @get:Input
    val yaml = YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
            .enable(YAMLGenerator.Feature.MINIMIZE_QUOTES)
            .enable(YAMLGenerator.Feature.INDENT_ARRAYS)

    init {
        group = "Spigradle"
        description = "Generate yaml file"
    }

    fun setOptionsToMap(any: Any) {
        options = Jackson.MAPPER.convertValue<Map<String, Any>>(any).toMutableMap()
    }

    @TaskAction
    fun generate() {
        file.bufferedWriter(Charset.forName(encoding)).use {
            ObjectMapper(yaml).writeValue(it, options)
        }
    }
}