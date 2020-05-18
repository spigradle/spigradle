@file:Suppress("UnstableApiUsage")

package kr.entree.spigradle.module.spigot

import groovy.lang.Closure
import kr.entree.spigradle.data.Dependencies
import kr.entree.spigradle.data.Dependency
import kr.entree.spigradle.internal.*
import kr.entree.spigradle.kotlin.maven
import kr.entree.spigradle.module.common.Messages
import kr.entree.spigradle.module.common.PLUGIN_APT_DEFAULT_PATH
import kr.entree.spigradle.module.common.SpigradlePlugin
import kr.entree.spigradle.module.common.task.GenerateYamlTask
import kr.entree.spigradle.module.spigot.data.SpigotDependencies
import kr.entree.spigradle.module.spigot.data.SpigotRepositories
import kr.entree.spigradle.module.spigot.data.setLoadGroovyExtension
import kr.entree.spigradle.module.spigot.extension.SpigotPluginDescription
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.internal.artifacts.dsl.DefaultRepositoryHandler.BINTRAY_JCENTER_URL
import org.gradle.api.plugins.ExtraPropertiesExtension
import org.gradle.language.jvm.tasks.ProcessResources
import java.io.File

/**
 * Created by JunHyung Lim on 2020-04-28
 */
class SpigotPlugin : Plugin<Project> { // TODO: Shortcuts, Plugin YAML Generation, Main class auto detection
    companion object {
        const val YAML_GEN_TASK_ID = "spigotPluginYaml"
    }

    override fun apply(project: Project) {
        with(project) {
            pluginManager.apply<SpigradlePlugin>()
            setupYamlGenTask()
            setupGroovyExtensions()
        }
    }

    private fun Project.setupYamlGenTask() {
        val description = extensions.create<SpigotPluginDescription>("spigot", this)
        val generateTask = tasks.create<GenerateYamlTask>(YAML_GEN_TASK_ID)
        Groovies.getExtensionFrom(description).setLoadGroovyExtension()
        generateTask.value = description
        val processResource = tasks.findByBoth<ProcessResources>("processResources") {
            from(generateTask.temporaryDir)
            finalizedBy(generateTask)
        } ?: return
        generateTask.doFirst {
            val descriptionFile = File(processResource.temporaryDir, "plugin.yml")
            if (descriptionFile.isFile && descriptionFile.name == generateTask.file.name) {
                it.enabled = false
            } else {
                validateDescription(description, this)
            }
        }
    }

    private fun validateDescription(description: SpigotPluginDescription, project: Project) {
        description.apply {
            main = main ?: project.getSpigotPluginMainOrThrow()
            name = name ?: project.name
            version = version ?: project.version.toString()
        }
    }

    private fun Project.getSpigotPluginMainOrThrow(): String? {
        val found = runCatching { // get APT result or find out
            File(project.buildDir, PLUGIN_APT_DEFAULT_PATH).readText()
        }.getOrNull() ?: findSpigotPluginMain()
        return found ?: throw GradleException(Messages.noMainFound("spigot", YAML_GEN_TASK_ID))
    }

    private fun findSpigotPluginMain(): String? {
        TODO()
    }

    private fun Project.setupGroovyExtensions() {
        extensions.findByBoth<ExtraPropertiesExtension>("ext")?.apply {
            setupDependencies()
            setupRepositories()
        }
    }

    private fun Project.setupRepositories() {
        val ext = Groovies.getExtensionFrom(repositories)
        SpigotRepositories.toFieldEntries<String>().forEach { (name, url) ->
            ext.set(name, object : Closure<Any>(this, this) {
                fun doCall() = repositories.maven(url)
            })
        }
        SpigotRepositories.run {
            listOf(SPIGOT_MC, SONATYPE, PAPER_MC, BINTRAY_JCENTER_URL)
        }.forEach {
            repositories.maven(it)
        }
    }

    private fun Project.setupDependencies() {
        val ext = Groovies.getExtensionFrom(dependencies)
        listOf(Dependencies, SpigotDependencies).flatMap {
            it.toFieldEntries<Dependency>()
        }.forEach { (name, dependency) ->
            ext.set(name, object : Closure<Any>(this, this) {
                fun doCall(version: String?) = dependency.format(version)
            })
        }
    }
}