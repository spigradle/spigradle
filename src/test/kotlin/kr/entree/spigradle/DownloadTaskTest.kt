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

import kr.entree.spigradle.module.spigot.SpigotDebugTask
import kr.entree.spigradle.module.spigot.SpigotDebugTask.downloadPaperTaskname
import kr.entree.spigradle.module.spigot.SpigotDebugTask.getPaperBuildsTaskname
import kr.entree.spigradle.module.spigot.SpigotDebugTask.getPaperDownloadsTaskname
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DownloadTaskTest {
    fun testGradleTask(taskName: String, dir: File, buildscript: String = """
        plugins {
            id 'kr.entree.spigradle'
        }
    """.trimIndent()) {
        File(dir, "build.gradle").writeText(buildscript)
        val result = GradleRunner.create()
            .withProjectDir(dir)
            .withArguments(taskName, "-s")
            .withPluginClasspath()
            .build()
        assertEquals(TaskOutcome.SUCCESS, result.task(":${taskName}")?.outcome)
    }

    @Test
    fun `get latest paper build`(@TempDir dir: File) {
        testGradleTask(getPaperBuildsTaskname, dir)
    }

    @Test
    fun `get latest paper downloads`(@TempDir dir: File) {
        testGradleTask(getPaperDownloadsTaskname, dir)
    }

    @Test
    fun `downloader latest paper`(@TempDir dir: File) {
        testGradleTask(downloadPaperTaskname, dir)
    }
}
