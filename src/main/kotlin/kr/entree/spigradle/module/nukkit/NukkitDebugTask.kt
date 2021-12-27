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

package kr.entree.spigradle.module.nukkit

import de.undercouch.gradle.tasks.download.Download
import kr.entree.spigradle.data.NukkitDebug
import kr.entree.spigradle.internal.applyToConfigure
import kr.entree.spigradle.module.common.DebugTask.registerPreparePlugins
import kr.entree.spigradle.module.common.DebugTask.registerRunServer

import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.register
import java.io.File

object NukkitDebugTask {
    const val TASK_GROUP_DEBUG = "nukkit debug"
    const val NUKKIT_X_URL = "https://ci.nukkitx.com/job/NukkitX/job/Nukkit/job/master/lastSuccessfulBuild/artifact/target/nukkit-1.0-SNAPSHOT.jar"

    fun Project.registerDownloadNukkit(debug: NukkitDebug): TaskProvider<Download> {
        return tasks.register("downloadNukkit", Download::class) {
            group = TASK_GROUP_DEBUG
            description = "Download the NukkitX"
            src(NUKKIT_X_URL)
            dest(provider { debug.serverJar })
        }
    }

    fun Project.registerPrepareNukkit(): TaskProvider<Task> {
        return tasks.register("prepareNukkit") {
            group = TASK_GROUP_DEBUG
            description = "Prepare the Nukkit"
        }
    }

    fun Project.registerRunNukkit(debug: NukkitDebug): TaskProvider<JavaExec> {
        return registerRunServer("Nukkit", debug).applyToConfigure {
            group = TASK_GROUP_DEBUG
            description = "Startup the NukkitX server."
            classpath = files(provider { debug.serverJar })
            setWorkingDir(provider { debug.serverDirectory })
        }
    }

    fun Project.registerPrepareNukkitPlugins(nukkit: NukkitExtension): TaskProvider<Copy> {
        return registerPreparePlugins(
                "prepareNukkitPlugin", // TODO: Rename to `prepareNukkitplugins`
                "plugin.yml",
                provider { nukkit.depends + nukkit.softDepends }
        ).applyToConfigure {
            group = TASK_GROUP_DEBUG
            outputs.files(fileTree(nukkit.debug.serverDirectory.resolve("plugins")) {
                include("*.jar")
            })
            into(provider { File(nukkit.debug.serverDirectory, "plugins") })
        }
    }

    fun Project.registerDebugNukkit(): TaskProvider<Task> {
        return tasks.register("debugNukkit") {
            group = TASK_GROUP_DEBUG
            description = "Startup the Nukkit with your plugins."
        }
    }
}