@file:Suppress("UnstableApiUsage")

package kr.entree.spigradle.spigot

import kr.entree.spigradle.data.Dependencies
import kr.entree.spigradle.data.all
import kr.entree.spigradle.data.format
import kr.entree.spigradle.spigot.extension.SpigotPluginDescriptor
import kr.entree.spigradle.task.YamlGenerateTask
import kr.entree.spigradle.util.create
import kr.entree.spigradle.util.findByBoth
import kr.entree.spigradle.util.withType
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtraPropertiesExtension
import org.gradle.language.jvm.tasks.ProcessResources

/**
 * Created by JunHyung Lim on 2020-04-28
 */
class SpigotPlugin : Plugin<Project> {
    companion object {
        const val YAML_GEN_TASK_ID = "spigotPluginYaml"
    }

    override fun apply(project: Project) {
        with(project) {
            setupTasks()
            setupExtensions()
            setupGroovyExtensions()
        }
    }

    private fun Project.setupExtensions() {
        val descriptor = extensions.create<SpigotPluginDescriptor>("spigot")
        tasks.findByBoth<YamlGenerateTask>(YAML_GEN_TASK_ID) {
            serializer = { it.stringify(SpigotPluginDescriptor.serializer(), descriptor) }
        }
    }

    private fun Project.setupTasks() {
        val generateSpigotYamlTask = tasks.create<YamlGenerateTask>(YAML_GEN_TASK_ID)
        tasks.withType<ProcessResources> {
            from(generateSpigotYamlTask.temporaryDir)
        }
    }

    private fun Project.setupGroovyExtensions() {
        extensions.findByBoth<ExtraPropertiesExtension>("ext")?.apply {
            setupDependencies()
            setupRepositories()
        }
    }

    private fun ExtraPropertiesExtension.setupDependencies() {
        Dependencies.all.forEach { (name, dependency) ->
            set(name, { input: String -> dependency.format(input) })
        }
    }

    private fun ExtraPropertiesExtension.setupRepositories() {
        TODO()
    }
}