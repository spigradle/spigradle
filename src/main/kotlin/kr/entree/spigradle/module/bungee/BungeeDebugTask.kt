package kr.entree.spigradle.module.bungee

import kr.entree.spigradle.data.BungeeDebug
import kr.entree.spigradle.internal.applyToConfigure
import kr.entree.spigradle.module.common.DebugTask.registerPreparePlugin
import kr.entree.spigradle.module.common.DebugTask.registerRunServer
import kr.entree.spigradle.module.common.Download
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.register
import java.io.File

/**
 * Created by JunHyung Lim on 2020-06-07
 */
object BungeeDebugTask {
    const val TASK_GROUP_DEBUG = "bungeecord debug"
    const val BUNGEECORD_URL = "https://ci.md-5.net/job/BungeeCord/lastSuccessfulBuild/artifact/bootstrap/target/BungeeCord.jar"

    fun Project.registerDownloadBungee(debug: BungeeDebug): TaskProvider<Download> {
        return tasks.register("downloadDebug", Download::class) {
            group = TASK_GROUP_DEBUG
            description = "Download the Bungeecord."
            source.set(BUNGEECORD_URL)
            destination.set(provider { debug.bungeeJar })
        }
    }

    fun Project.registerRunBungee(debug: BungeeDebug): TaskProvider<JavaExec> {
        return registerRunServer("runBungee", debug.agentPort).applyToConfigure {
            group = TASK_GROUP_DEBUG
            description = "Startup the bunge server."
            classpath = files(provider { debug.bungeeJar })
            setWorkingDir(provider { debug.bungeeDirectory })
        }
    }

    fun Project.registerPrepareBungeePlugins(bungee: BungeeExtension): TaskProvider<Copy> {
        return registerPreparePlugin(
                "prepareBungeePlugin",
                "name",
                { bungee.depends + bungee.softDepends },
                { it.readBungeePluginDescription() }
        ).applyToConfigure {
            group = TASK_GROUP_DEBUG
            into(provider { File(bungee.debug.bungeeDirectory, "plugins") })
        }
    }

    fun Project.registerDebugBungee(): TaskProvider<Task> {
        return tasks.register("debugBungee") {
            group = TASK_GROUP_DEBUG
            description = "Startup the bungeecord with your plugin.jar"
        }
    }
}