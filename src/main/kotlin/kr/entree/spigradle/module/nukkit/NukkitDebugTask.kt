package kr.entree.spigradle.module.nukkit

import kr.entree.spigradle.data.NukkitDebug
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
 * Created by JunHyung Lim on 2020-06-08
 */
object NukkitDebugTask {
    const val TASK_GROUP_DEBUG = "nukkit debug"
    const val NUKKIT_X_URL = "https://ci.nukkitx.com/job/NukkitX/job/Nukkit/job/master/lastSuccessfulBuild/artifact/target/nukkit-1.0-SNAPSHOT.jar"

    fun Project.registerDownloadNukkit(debug: NukkitDebug): TaskProvider<Download> {
        return tasks.register("downloadNukkit", Download::class) {
            group = TASK_GROUP_DEBUG
            description = "Download the NukkitX"
            source.set(NUKKIT_X_URL)
            destination.set(provider { debug.nukkitJar })
        }
    }

    fun Project.registerRunNukkit(debug: NukkitDebug): TaskProvider<JavaExec> {
        return registerRunServer("runNukkit") { debug.agentPort }.applyToConfigure {
            group = TASK_GROUP_DEBUG
            description = "Startup the NukkitX server."
            classpath = files(provider { debug.nukkitJar })
            setWorkingDir(provider { debug.nukkitDirectory })
        }
    }

    fun Project.registerPrepareNukkitPlugins(nukkit: NukkitExtension): TaskProvider<Copy> {
        return registerPreparePlugin(
                "prepareNukkitPlugin",
                "name",
                "plugin.yml"
        ) { nukkit.depends + nukkit.softDepends }.applyToConfigure {
            group = TASK_GROUP_DEBUG
            into(provider { File(nukkit.debug.nukkitDirectory, "plugins") })
        }
    }

    fun Project.registerDebugNukkit(): TaskProvider<Task> {
        return tasks.register("debugNukkit") {
            group = TASK_GROUP_DEBUG
            description = "Startup the Nukkit with your plugins."
        }
    }
}