@file:Suppress("UnstableApiUsage")

package kr.entree.spigradle.module.spigot

import kr.entree.spigradle.data.Dependencies
import kr.entree.spigradle.data.Dependency
import kr.entree.spigradle.internal.create
import kr.entree.spigradle.internal.findByBoth
import kr.entree.spigradle.internal.toFieldEntries
import kr.entree.spigradle.internal.withType
import kr.entree.spigradle.module.common.task.GenerateYamlTask
import kr.entree.spigradle.module.spigot.data.SpigotDependencies
import kr.entree.spigradle.module.spigot.extension.SpigotPluginAttributes
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtraPropertiesExtension
import org.gradle.language.jvm.tasks.ProcessResources

/**
 * Created by JunHyung Lim on 2020-04-28
 */
private const val YAML_GEN_TASK_ID = "spigotPluginYaml"

class SpigotPlugin : Plugin<Project> { // TODO: Shortcuts, Plugin YAML Generation, Main class auto detection
    override fun apply(project: Project) {
        with(project) {
            setupTasks()
            setupExtensions()
            setupGroovyExtensions()
        }
    }

    private fun Project.setupTasks() {
        val generateSpigotYamlTask = tasks.create<GenerateYamlTask>(YAML_GEN_TASK_ID)
        tasks.withType<ProcessResources> {
            from(generateSpigotYamlTask)
        }
    }

    private fun Project.setupExtensions() {
        val descriptor = extensions.create<SpigotPluginAttributes>("spigot")
        tasks.findByBoth<GenerateYamlTask>(YAML_GEN_TASK_ID) {
            value = descriptor
        }
    }

    private fun Project.setupGroovyExtensions() {
        extensions.findByBoth<ExtraPropertiesExtension>("ext")?.apply {
            setupDependencies()
            setupRepositories()
        }
    }

    private fun Project.setupDependencies() {
        val ext = dependencies.extensions.findByBoth<ExtraPropertiesExtension>("ext") ?: return
        listOf(Dependencies, SpigotDependencies).flatMap {
            it.toFieldEntries<Dependency>()
        }.forEach { (name, dependency) ->
            ext.set(name, { input: String -> dependency.format(input) })
        }
    }

    private fun Project.setupRepositories() {

    }
}