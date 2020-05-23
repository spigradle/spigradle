package kr.entree.spigradle.module.nukkit

import kr.entree.spigradle.data.Load
import kr.entree.spigradle.data.NukkitRepositories
import kr.entree.spigradle.internal.Groovies
import kr.entree.spigradle.module.common.applySpigradlePlugin
import kr.entree.spigradle.module.common.setupDescGenTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.maven

/**
 * Created by JunHyung Lim on 2020-04-28
 */
class NukkitPlugin : Plugin<Project> {
    companion object {
        const val DESC_GEN_TASK_NAME = "generateNukkitDescription"
        const val MAIN_DETECTION_TASK_NAME = "detectNukkitMain"
        const val EXTENSION_NAME = "nukkit"
        const val DESC_FILE_NAME = "plugin.yml"
        const val PLUGIN_SUPER_CLASS = "cn/nukkit/plugin/PluginBase"
    }

    override fun apply(project: Project) {
        with(project) {
            applySpigradlePlugin()
            setupDefaultRepositories()
            setupDescGenTask<NukkitDescription>(
                    EXTENSION_NAME,
                    DESC_GEN_TASK_NAME,
                    MAIN_DETECTION_TASK_NAME,
                    DESC_FILE_NAME,
                    PLUGIN_SUPER_CLASS
            )
            setupGroovyExtensions()
        }
    }

    private fun Project.setupDefaultRepositories() {
        repositories.maven(NukkitRepositories.NUKKIT_X)
    }

    private fun Project.setupGroovyExtensions() {
        Groovies.getExtensionFrom(extensions.getByName(EXTENSION_NAME)).apply {
            set("POST_WORLD", Load.POST_WORLD)
            set("STARTUP", Load.STARTUP)
        }
    }
}