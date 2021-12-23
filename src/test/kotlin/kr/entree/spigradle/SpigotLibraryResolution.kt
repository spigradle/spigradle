/*
 * Copyright (c) 2021 Spigradle contributors.
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

import kr.entree.spigradle.util.testGradleTask
import org.junit.jupiter.api.io.TempDir
import org.yaml.snakeyaml.Yaml
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals

class SpigotLibraryResolution {
    @Test
    fun `resolve libraries and serialize`(@TempDir dir: File) {
        val okhttp = "com.squareup.okhttp3:okhttp:4.9.0"
        testGradleTask("generateSpigotDescription", dir, """
            plugins {
                id 'kr.entree.spigradle'
            }
            
            repositories {
                mavenCentral()
            }
            
            spigot {
                main = 'MyMain'
            }
            
            dependencies {
                compileOnly(spigot('1.18.1'))
                implementation("$okhttp")
            }
        """.trimIndent())
        val ymlFile = dir.resolve("build").resolve("tmp").resolve("generateSpigotDescription").resolve("plugin.yml")
        val yaml = Yaml().load<Map<String, Any>>(ymlFile.readText())
        val libs = yaml["libraries"] as? List<String>
        assertEquals(okhttp, libs?.get(0))
        assertEquals(1, libs?.size ?: 0)
    }

    @Test
    fun `ignore resolution if the property presented`(@TempDir dir: File) {
        val dep = "me.mygroup:myname:1.0.0"
        testGradleTask("generateSpigotDescription", dir, """
            plugins {
                id 'kr.entree.spigradle'
            }
            
            repositories {
                mavenCentral()
            }
            
            spigot {
                main = 'MyMain'
                libraries = ['${dep}']
            }
            
            dependencies {
                compileOnly(spigot('1.18.1'))
                implementation("com.squareup.okhttp3:okhttp:4.9.0")
            }
        """.trimIndent())
        val ymlFile = dir.resolve("build").resolve("tmp").resolve("generateSpigotDescription").resolve("plugin.yml")
        val yaml = Yaml().load<Map<String, Any>>(ymlFile.readText())
        val libs = yaml["libraries"] as? List<String>
        assertEquals(dep, libs?.get(0))
        assertEquals(1, libs?.size ?: 0)
    }
}
