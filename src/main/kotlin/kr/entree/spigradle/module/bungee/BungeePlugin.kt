package kr.entree.spigradle.module.bungee

import kr.entree.spigradle.data.Repositories
import kr.entree.spigradle.module.common.applySpigradlePlugin
import kr.entree.spigradle.module.common.registerDescGenTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.maven

/**
 * Created by JunHyung Lim on 2020-04-28
 */
class BungeePlugin : Plugin<Project> {
    companion object {
        const val DESC_GEN_TASK_NAME = "generateBungeeDescription"
        const val MAIN_DETECTION_TASK_NAME = "detectBungeeMain"
        const val EXTENSION_NAME = "bungee"
        const val DESC_FILE_NAME = "bungee.yml"
        const val PLUGIN_SUPER_CLASS = "net/md_5/bungee/api/plugin/Plugin"
    }

    override fun apply(project: Project) {
        with(project) {
            applySpigradlePlugin()
            setupDefaultRepositories()
            registerDescGenTask<BungeeExtension>(
                    EXTENSION_NAME,
                    DESC_GEN_TASK_NAME,
                    MAIN_DETECTION_TASK_NAME,
                    DESC_FILE_NAME,
                    PLUGIN_SUPER_CLASS
            )
        }
    }

    private fun Project.setupDefaultRepositories() {
        repositories.maven(Repositories.SONATYPE)
    }
}