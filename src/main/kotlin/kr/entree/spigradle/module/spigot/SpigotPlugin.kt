package kr.entree.spigradle.module.spigot

import kr.entree.spigradle.data.setSpigotExtension
import kr.entree.spigradle.internal.Groovies
import kr.entree.spigradle.module.common.SpigradlePlugin
import kr.entree.spigradle.module.common.task.GenerateYamlTask
import kr.entree.spigradle.module.common.task.SubclassDetectionTask
import kr.entree.spigradle.module.spigot.extension.SpigotDescription
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.provideDelegate

/**
 * Created by JunHyung Lim on 2020-04-28
 */
class SpigotPlugin : Plugin<Project> { // TODO: Shortcuts, Plugin YAML Generation, Main class auto detection
    companion object {
        const val YAML_GEN_TASK_NAME = "spigotPluginYaml"
        const val MAIN_DETECTION_TASK_NAME = "spigotMainDetection"
        const val EXTENSION_NAME = "spigot"
        const val BUKKIT_PLUGIN_SUPER_CLASS = "org/bukkit/plugin/java/JavaPlugin"
    }

    override fun apply(project: Project) {
        with(project) {
            pluginManager.apply(SpigradlePlugin::class)
            setupYamlGenTask()
        }
    }

    private fun Project.setupYamlGenTask() {
        val description = extensions.create<SpigotDescription>(EXTENSION_NAME, this)
        val detectionTask = SubclassDetectionTask.create(this, MAIN_DETECTION_TASK_NAME, BUKKIT_PLUGIN_SUPER_CLASS)
        val generateTask = GenerateYamlTask.create(this, YAML_GEN_TASK_NAME, EXTENSION_NAME, description)
        Groovies.getExtensionFrom(description).setSpigotExtension()
        val classes: Task by tasks
        classes.finalizedBy(detectionTask)
        generateTask.dependsOn(detectionTask) // classes -> detectionTask -> generateTask
    }
}