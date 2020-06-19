package kr.entree.spigradle.module.spigot

import kr.entree.spigradle.data.Load
import kr.entree.spigradle.data.SpigotRepositories
import kr.entree.spigradle.internal.Groovies
import kr.entree.spigradle.internal.applyToConfigure
import kr.entree.spigradle.module.common.applySpigradlePlugin
import kr.entree.spigradle.module.common.createDebugConfigurations
import kr.entree.spigradle.module.common.registerDescGenTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByName
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.maven
import org.gradle.kotlin.dsl.provideDelegate

/**
 * The Spigot plugin that adds:
 * - [kr.entree.spigradle.module.common.YamlGenerate] task for the 'plugin.yml' generation.
 * - [kr.entree.spigradle.module.common.SubclassDetection] task for the main-class detection.
 * - Debug tasks for test your plugin.
 */
class SpigotPlugin : Plugin<Project> {
    companion object {
        const val DESC_GEN_TASK_NAME = "generateSpigotDescription"
        const val MAIN_DETECTION_TASK_NAME = "detectSpigotMain"
        const val EXTENSION_NAME = "spigot"
        const val DESC_FILE_NAME = "plugin.yml"
        const val PLUGIN_SUPER_CLASS = "org/bukkit/plugin/java/JavaPlugin"
        const val TASK_GROUP = "spigot"
    }

    private val Project.spigot get() = extensions.getByName<SpigotExtension>("spigot")

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
            createDebugConfigurations("Spigot", spigot.debug)
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
        val debugOption = spigot.debug
        // prepareSpigot: downloadBuildTools -> buildSpigot -> copySpigot
        // preparePlugin: copyArtifactJar -> copyClasspathPlugins
        // debugSpigot: preparePlugin -> prepareSpigot -> runSpigot
        // debugPaper: preparePlugin -> downloadPaperclip -> runSpigot(runPaper)
        with(SpigotDebugTask) {
            // Spigot
            val buildToolDownload = registerDownloadBuildTool(debugOption)
            val buildSpigot = registerBuildSpigot(debugOption).applyToConfigure {
                mustRunAfter(buildToolDownload)
            }
            val prepareSpigot = registerPrepareSpigot(debugOption).applyToConfigure {
                dependsOn(buildToolDownload, buildSpigot)
            }
            val build by tasks
            val preparePlugin = registerPrepareSpigotPlugin(spigot).applyToConfigure {
                dependsOn(build)
            }
            val runSpigot = registerRunSpigot(debugOption).applyToConfigure {
                mustRunAfter(preparePlugin)
            }
            registerDebugRun("Spigot").applyToConfigure { // debugSpigot
                dependsOn(preparePlugin, prepareSpigot, runSpigot)
                runSpigot.get().mustRunAfter(prepareSpigot)
            }
            registerCleanSpigotBuild(debugOption)
            // Paper
            val paperClipDownload = registerDownloadPaper(debugOption)
            registerDebugRun("Paper").applyToConfigure { // debugPaper
                dependsOn(preparePlugin, paperClipDownload, runSpigot)
                runSpigot.get().mustRunAfter(paperClipDownload)
            }
        }
    }
}