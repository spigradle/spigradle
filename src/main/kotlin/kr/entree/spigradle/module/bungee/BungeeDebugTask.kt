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

package kr.entree.spigradle.module.bungee

import de.undercouch.gradle.tasks.download.Download
import kr.entree.spigradle.data.BungeeDebug
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

object BungeeDebugTask {
    const val TASK_GROUP_DEBUG = "bungee debug"
    const val BUNGEECORD_URL = "https://ci.md-5.net/job/BungeeCord/lastSuccessfulBuild/artifact/bootstrap/target/BungeeCord.jar"

    fun Project.registerDownloadBungee(debug: BungeeDebug): TaskProvider<Download> {
        return tasks.register("downloadBungee", Download::class) {
            group = TASK_GROUP_DEBUG
            description = "Download the Bungeecord."
            src(BUNGEECORD_URL)
            dest(provider { debug.serverJar })
        }
    }

    fun Project.registerRunBungee(debug: BungeeDebug): TaskProvider<JavaExec> {
        return registerRunServer("Bungee", debug).applyToConfigure {
            group = TASK_GROUP_DEBUG
            description = "Startup the Bungeecord server."
            classpath = files(provider { debug.serverJar })
            setWorkingDir(provider { debug.serverDirectory })
        }
    }

    fun Project.registerPrepareBungee(): TaskProvider<Task> {
        return tasks.register("prepareBungee") {
            group = TASK_GROUP_DEBUG
            description = "Prepare the Bungeecord."
        }
    }

    fun Project.registerPrepareBungeePlugins(bungee: BungeeExtension): TaskProvider<Copy> {
        return registerPreparePlugins(
                "prepareBungeePlugin", // TODO: Rename to `prepareBungeePlugins`
                "bungee.yml",
                provider { bungee.depends + bungee.softDepends }
        ).applyToConfigure {
            group = TASK_GROUP_DEBUG
            outputs.files(fileTree(bungee.debug.serverDirectory.resolve("plugins")) {
                include("*.jar")
            })
            into(provider { File(bungee.debug.serverDirectory, "plugins") })
        }
    }

    fun Project.registerDebugBungee(): TaskProvider<Task> {
        return tasks.register("debugBungee") {
            group = TASK_GROUP_DEBUG
            description = "Startup the Bungeecord with your plugins."
        }
    }
}