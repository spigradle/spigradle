package kr.entree.spigradle.task

import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.TaskContainer
import java.io.File
import java.nio.charset.Charset

/**
 * Created by JunHyung Lim on 2020-04-28
 */
class YamlGenerateTask : DefaultTask() {
    var serializer: (Yaml) -> String = { "" }
    var encoding: Charset = Charsets.UTF_8
    var file: File = File(temporaryDir, "plugin.yml")
    var yaml: Yaml = Yaml(configuration = YamlConfiguration(encodeDefaults = false))

    init {
        group = "Spigradle"
        description = "Generate yaml file"
    }

    @TaskAction
    fun generate() {
        file.bufferedWriter(encoding).use {
            it.write(serializer(yaml))
        }
    }
}