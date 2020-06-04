package kr.entree.spigradle.module.spigot

import kr.entree.spigradle.data.Load
import kr.entree.spigradle.data.SpigotRepositories
import kr.entree.spigradle.internal.Groovies
import kr.entree.spigradle.internal.applyToConfigure
import kr.entree.spigradle.module.common.applySpigradlePlugin
import kr.entree.spigradle.module.common.registerDescGenTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.maven
import org.gradle.kotlin.dsl.provideDelegate

/**
 * Created by JunHyung Lim on 2020-04-28
 */
class SpigotPlugin : Plugin<Project> {
    companion object {
        const val DESC_GEN_TASK_NAME = "generateSpigotDescription"
        const val MAIN_DETECTION_TASK_NAME = "detectSpigotMain"
        const val EXTENSION_NAME = "spigot"
        const val DESC_FILE_NAME = "plugin.yml"
        const val PLUGIN_SUPER_CLASS = "org/bukkit/plugin/java/JavaPlugin"
        const val BUILD_TOOLS_URL = "https://hub.spigotmc.org/jenkins/job/BuildTools/lastSuccessfulBuild/artifact/target/BuildTools.jar"
        const val TASK_GROUP = "spigot"
        const val TASK_GROUP_DEBUG = "$TASK_GROUP debug"
    }

    override fun apply(project: Project) {
        with(project) {
            applySpigradlePlugin()
            setupDefaultRepositories()
            registerDescGenTask<SpigotExtension>(
                    EXTENSION_NAME,
                    DESC_GEN_TASK_NAME,
                    MAIN_DETECTION_TASK_NAME,
                    DESC_FILE_NAME,
                    PLUGIN_SUPER_CLASS
            )
            setupGroovyExtensions()
            setupSpigotDebugTasks()
        }
    }

    private fun Project.setupDefaultRepositories() {
        SpigotRepositories.run {
            listOf(SPIGOT_MC, PAPER_MC)
        }.forEach {
            repositories.maven(it)
        }
    }

    private fun Project.setupGroovyExtensions() {
        Groovies.getExtensionFrom(extensions.getByName(EXTENSION_NAME)).apply {
            set("POST_WORLD", Load.POST_WORLD)
            set("STARTUP", Load.STARTUP)
        }
    }

    private fun Project.setupSpigotDebugTasks() {
        val spigot: SpigotExtension by extensions
        val debugOption = spigot.debug
        // prepareSpigot: downloadBuildTools -> buildSpigot -> copySpigot
        // preparePlugin: copyArtifactJar -> copyClasspathPlugins
        // debugSpigot: preparePlugin -> prepareSpigot -> runSpigot
        // debugPaper: preparePlugin -> downloadPaperclip -> runSpigot(runPaper)
        with(SpigotDebugTask) {
            // Spigot
            val buildToolDownload = createDownloadBuildTool(debugOption)
            val buildSpigot = createBuildSpigot(debugOption).applyToConfigure {
                mustRunAfter(buildToolDownload)
            }
            val prepareSpigot = createPrepareSpigot(debugOption).applyToConfigure {
                dependsOn(buildToolDownload, buildSpigot)
            }
            val build by tasks
            val preparePlugin = createPreparePlugin(spigot).applyToConfigure {
                dependsOn(build)
            }
            val runSpigot = createRunSpigot(debugOption).applyToConfigure {
                mustRunAfter(preparePlugin)
            }
            createDebugRun("Spigot").applyToConfigure { // debugSpigot
                dependsOn(preparePlugin, prepareSpigot, runSpigot)
                runSpigot.get().mustRunAfter(prepareSpigot)
            }
            createCleanSpigotBuild(debugOption)
            // Paper
            val paperClipDownload = createDownloadPaper(debugOption)
            createDebugRun("Paper").applyToConfigure { // debugPaper
                dependsOn(preparePlugin, paperClipDownload, runSpigot)
                runSpigot.get().mustRunAfter(paperClipDownload)
            }
        }
    }
}