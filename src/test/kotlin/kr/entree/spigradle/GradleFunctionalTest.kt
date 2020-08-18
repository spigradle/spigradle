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
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.io.TempDir
import java.io.File
import kotlin.test.BeforeTest
import kotlin.test.Test

/**
 * Created by JunHyung Im on 2020-08-18
 */
internal fun File.create(): File = apply {
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

    private fun createGradleRunner() = GradleRunner.create()
            .withProjectDir(dir)
            .withPluginClasspath()

    @BeforeTest
    fun setup() {
        buildFile = dir.resolve("build.gradle").create()
        settingsFile = dir.resolve("settings.gradle").create().writeGroovy("""
            rootProject.name = 'abc'
            include('sub')
        """.trimIndent())
        subBuildFile = dir.resolve("sub/build.gradle").create()
        subSettingsFile = dir.resolve("sub/settings.gradle").create().writeGroovy("""
            rootProject.name = 'functional-test-sub'
        """.trimIndent())
    }

    @Test
    fun `apply scala and spigradle on a subproject`() {
        subBuildFile.writeGroovy("""
            plugins {
                id 'scala'
                id 'kr.entree.spigradle'
            }
            spigot.main = 'empty'
        """.trimIndent())
        assertDoesNotThrow {
            createGradleRunner().build()
        }
    }
}