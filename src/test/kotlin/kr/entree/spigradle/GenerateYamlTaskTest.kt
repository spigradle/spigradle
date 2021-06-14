package kr.entree.spigradle

import kr.entree.spigradle.data.Load
import kr.entree.spigradle.module.common.YamlGenerate
import kr.entree.spigradle.module.spigot.SpigotExtension
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.support.normaliseLineSeparators
import org.gradle.testfixtures.ProjectBuilder
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Created by JunHyung Lim on 2020-05-13
 */
class GenerateYamlTaskTest {
    val project = ProjectBuilder.builder().build()
    val yamlTask = project.tasks.create("yaml", YamlGenerate::class)
    val file get() = File(yamlTask.temporaryDir, "plugin.yml")

    init {
        yamlTask.outputFiles.forEach {
            it.deleteOnExit()
        }
    }

    @Test
    fun `simple generation`() {
        val pairs = listOf("value" to "test contents")
        yamlTask.apply {
            properties.set(pairs.toMap().toMutableMap())
            outputFiles.from(file)
            generate()
        }
        val text = pairs.joinToString { (key, value) -> "$key: $value\n" }
        assertEquals(text, file.readText())
    }

    @Test
    fun `simple serialization`() {
        val extension = project.extensions.create<SpigotExtension>("spigot", project).apply {
            main = "SpigradleMain"
        }
        yamlTask.apply {
            outputFiles.from(file)
            serialize(extension)
            generate()
        }
        assertEquals("main: SpigradleMain\n", file.readText())
    }

    @Test
    fun `detail serialization`() {
        val extension = project.extensions.create<SpigotExtension>("spigot", project).apply {
            main = "kr.entree.spigradle.Main"
            name = "Spigradle"
            version = "1.1"
            description = "This plugin does so much stuff it can't be contained!"
            website = "https://github.com/spigradle/spigradle"
            authors = listOf("EntryPoint")
            apiVersion = "1.15"
            load = Load.POST_WORLD
            prefix = "Its prefix"
            softDepends = listOf("ProtocolLib")
            loadBefore = listOf("ABC")
            libraries = listOf(
                "com.squareup.okhttp3:okhttp:4.9.0",
                "a:b:1.0.0"
            )
            commands.apply {
                create("give").apply {
                    description = "Give command."
                    usage = "/<command> [test|stop]"
                    permission = "test.foo"
                    permissionMessage = "You do not have permission!"
                    aliases = listOf("alias")
                }
            }
            permissions.apply {
                create("test.*").apply {
                    description = "Wildcard permission"
                    defaults = "op"
                    children = mapOf("test.foo" to true)
                }
                create("test.foo").apply {
                    description = "Allows foo command"
                    defaults = "true"
                }
            }
        }
        yamlTask.apply {
            outputFiles.from(file)
            serialize(extension)
            generate()
        }
        val expected =
            javaClass.getResourceAsStream("/spigot/plugin.yml").bufferedReader().readText().normaliseLineSeparators()
        assertEquals(expected, file.readText())
    }
}