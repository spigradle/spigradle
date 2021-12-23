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

import kr.entree.spigradle.data.SpigotDebug
import kr.entree.spigradle.data.SpigotDependencies
import kr.entree.spigradle.kotlin.spigot
import kr.entree.spigradle.module.common.applySpigradlePlugin
import kr.entree.spigradle.module.spigot.SpigotDebugTask
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.testfixtures.ProjectBuilder
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals

class BuildVersionInference {
    val debug = SpigotDebug(
        File(File("parent"), "child"),
        File(File("parent"), "child")
    )

    @Test
    fun `sort versions`() {
        val vers = listOf(
            "1.16.5",
            "1.16.4",
            "1.16.3",
            "1.16.2",
            "1.16.1",
            "1.15.2",
            "1.15.1",
            "1.15",
            "1.14.4",
            "1.14.3",
            "1.14.2",
            "1.14.1",
            "1.14",
            "1.13.2",
            "1.13.1",
            "1.13-pre7",
            "1.12.2",
            "1.12.1",
            "1.12",
            "1.11.2",
            "1.10.2",
            "1.9.4",
            "1.8.8"
        )
        assertEquals(
            vers,
            SpigotDebugTask.sortDescendingVersion(vers.shuffled())
        )
    }

    @Test
    fun gradle() {
        val ver = "0.1.0-R0.1-SNAPSHOT"
        val bigVer = "0.1.1-R0.1-SNAPSHOT"
        val smallVer = "0.0.1-R0.1-SNAPSHOT"
        listOf("compileOnly", "implementation").forEach {
            listOf(
                SpigotDependencies.SPIGOT,
                SpigotDependencies.SPIGOT_ALL,
                SpigotDependencies.PAPER
            ).forEach { dep ->
                prepareProject().run {
                    dependencies {
                        add(it, dep.format(ver))
                    }
                    assertEquals(ver, SpigotDebugTask.run { getBuildVersion(debug) }, "$it ${dep.format(ver)}")
                    dependencies {
                        add(it, dep.format(bigVer))
                    }
                    assertEquals(bigVer, SpigotDebugTask.run { getBuildVersion(debug) }, "$it ${dep.format(bigVer)}")
                    dependencies {
                        add(it, dep.format(smallVer))
                    }
                    assertEquals(bigVer, SpigotDebugTask.run { getBuildVersion(debug) }, "$it ${dep.format(bigVer)}")
                }
            }
        }
    }

    @Test
    fun `gradle 1_16_5 spigot`() {
        val project = prepareProject()
        project.dependencies {
            spigot("1.16.5")
        }
        SpigotDebugTask.apply {
            project.getBuildVersion(debug)
        }
    }

    fun prepareProject(): Project {
        val project = ProjectBuilder.builder().build()
        project.applySpigradlePlugin()
        return project
    }
}