package kr.entree.spigradle.module.common.task

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator
import com.fasterxml.jackson.module.kotlin.KotlinModule
import kr.entree.spigradle.internal.GeneratedSubclassSerializer
import kr.entree.spigradle.internal.NamedDomainObjectContainerSerializer
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.nio.charset.Charset

/**
 * Created by JunHyung Lim on 2020-04-28
 */
open class GenerateYamlTask : DefaultTask() {
    @get:Internal
    lateinit var value: Any
    @get:Internal
    var encoding: Charset = Charsets.UTF_8
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

    @TaskAction
    fun generate() {
        val gradleModule = SimpleModule()
                .addSerializer(NamedDomainObjectContainerSerializer())
                .addSerializer(GeneratedSubclassSerializer())
        val mapper = ObjectMapper(yaml)
                .registerModules(KotlinModule(), gradleModule)
                .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
        file.bufferedWriter(encoding).use {
            mapper.writeValue(it, value)
        }
    }
}