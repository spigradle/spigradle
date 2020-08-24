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

package kr.entree.spigradle

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.io.TempDir
import java.io.File
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Created by JunHyung Im on 2020-08-18
 */
internal fun File.createDirectories(): File = apply {
    parentFile.mkdirs()
    createNewFile()
}

internal fun File.writeGroovy(@Language("Groovy") contents: String): File = apply { writeText(contents) }

class GradleFunctionalTest {
    @TempDir
    lateinit var dir: File
    lateinit var buildFile: File
    lateinit var settingsFile: File
    lateinit var subBuildFile: File
    lateinit var subSettingsFile: File
    lateinit var javaFile: File
    lateinit var subJavaFile: File

    private fun createGradleRunner() = GradleRunner.create()
            .withProjectDir(dir)
            .withPluginClasspath()

    @BeforeTest
    fun setup() {
        buildFile = dir.resolve("build.gradle").createDirectories()
        settingsFile = dir.resolve("settings.gradle").createDirectories().writeGroovy("""
            rootProject.name = 'main'
            include('sub')
        """.trimIndent())
        subBuildFile = dir.resolve("sub/build.gradle").createDirectories()
        subSettingsFile = dir.resolve("sub/settings.gradle").createDirectories()
        javaFile = dir.resolve("src/main/java/Main.java").createDirectories()
        subJavaFile = dir.resolve("sub/src/main/java/Main.java").createDirectories()
    }

    @Test
    fun `apply scala and spigradle on a subproject`() {
        subBuildFile.writeGroovy("""
            plugins {
                id 'scala'
                id 'kr.entree.spigradle'
            }
            spigot.main = 'Main'
        """.trimIndent())
        assertDoesNotThrow {
            createGradleRunner().build()
        }
    }

    @Test
    fun `transitive prepare plugins`() {
        buildFile.writeGroovy("""
            plugins {
                id 'java-library'
                id 'kr.entree.spigradle'
            }
            spigot {
                main 'Main'
                depends 'WorldEdit', 'WorldGuard'
            }
            repositories {
                enginehub() 
            }
            dependencies {
                compileOnly worldedit('7.1.0')
                compileOnly worldguard('7.0.3')
            }
        """.trimIndent())
        //language=Groovy
        subBuildFile.writeGroovy("""
            plugins {
                id 'java'
                id 'kr.entree.spigradle'
            }
            spigot {
                main 'Sub'
                depends 'main', 'CommandHelper'
            }
            repositories {
                enginehub()
            }
            dependencies {
                compileOnly rootProject
                compileOnly commandhelper('3.3.4-SNAPSHOT')
            }
            prepareSpigotPlugins {
                doLast {
                    def baseDir = file("${'$'}{spigot.debug.serverDirectory}/plugins")
                    [
                        'main', 
                        'worldguard-bukkit-7.0.3', 
                        'commandhelper-3.3.4-SNAPSHOT',
                        ['worldedit-bukkit-7.1.0', 'worldedit-bukkit-7.1.0-SNAPSHOT']
                    ].each { name ->
                        def names = name instanceof List<?>
                            ? name as List<String>
                            : [name.toString()]
                        assert names.collect { 
                            file("${'$'}baseDir/${'$'}{it}.jar")
                        }.any {
                            it.isFile()
                        }
                    }
                }
            }
        """.trimIndent())
        subJavaFile.writeText("""
            public class Main {}
        """.trimIndent())
        val result = createGradleRunner().withArguments("prepareSpigotPlugins").build()
        assertEquals(TaskOutcome.SUCCESS, result.task(":sub:prepareSpigotPlugins")?.outcome)
    }

    @Test
    fun `test description default value`() {
        buildFile.writeGroovy("""
            plugins {
                id 'java'
                id 'kr.entree.spigradle'
            }
            description 'My awesome plugin'
            version '3.2.1'
            spigot.main = 'AwesomePlugin'
            generateSpigotDescription { doLast {
                [
                    "main": "AwesomePlugin",
                    "version": project.version,
                    "description": project.description
                ].each { k, v -> assert properties[k]?.getOrNull() == v }
            } }
        """.trimIndent())
        val result = createGradleRunner().withArguments("generateSpigotDescription").build()
        assertEquals(TaskOutcome.SUCCESS, result.task(":generateSpigotDescription")?.outcome)
    }

    @Test
    fun `test task configSpigot`() {
        dir.resolve("debug/spigot/spigot.yml").createDirectories().writeText(javaClass.getResource("/spigot/spigot.yml").readText())
        buildFile.writeGroovy("""
            plugins {
                id 'java'
                id 'kr.entree.spigradle'
            }
            spigot.main = 'AwesomePlugin'
            spigot.debug.eula = true
            import com.fasterxml.jackson.core.type.TypeReference
            import kr.entree.spigradle.internal.Jackson
            configSpigot {
                properties.put("mykey", "myval")            
                def file = new File(spigot.debug.serverDirectory, 'spigot.yml')
                def getter = { Jackson.YAML.readValue(file, new TypeReference<Map<String, Object>>() { }) }
                doFirst { assert getter()["settings"]["restart-on-crash"] == true }
                doLast { assert getter()["settings"]["restart-on-crash"] == false }
                doLast { assert getter()["mykey"] == "myval" }
            }
        """.trimIndent())
        val result = createGradleRunner().withArguments("configSpigot", "-s").build()
        assertEquals(TaskOutcome.SUCCESS, result.task(":configSpigot")?.outcome)
    }
}