package kr.entree.spigradle.module.spigot

import groovy.lang.Closure
import kr.entree.spigradle.data.Dependencies
import kr.entree.spigradle.data.Dependency
import kr.entree.spigradle.internal.Groovies
import kr.entree.spigradle.internal.Messages
import kr.entree.spigradle.internal.PLUGIN_APT_DEFAULT_PATH
import kr.entree.spigradle.internal.toFieldEntries
import kr.entree.spigradle.module.common.SpigradlePlugin
import kr.entree.spigradle.module.common.task.GenerateYamlTask
import kr.entree.spigradle.module.spigot.data.SpigotDependencies
import kr.entree.spigradle.module.spigot.data.SpigotRepositories
import kr.entree.spigradle.module.spigot.data.setSpigotExtension
import kr.entree.spigradle.module.spigot.extension.SpigotPluginDescription
import notNull
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.internal.artifacts.dsl.DefaultRepositoryHandler.BINTRAY_JCENTER_URL
import org.gradle.api.tasks.compile.AbstractCompile
import org.gradle.kotlin.dsl.*
import org.gradle.language.jvm.tasks.ProcessResources
import java.io.File

/**
 * Created by JunHyung Lim on 2020-04-28
 */
@Suppress("UnstableApiUsage")
class SpigotPlugin : Plugin<Project> { // TODO: Shortcuts, Plugin YAML Generation, Main class auto detection
    companion object {
        const val YAML_GEN_TASK_NAME = "spigotPluginYaml"
        const val EXTENSION_NAME = "spigot"
    }

    override fun apply(project: Project) {
        with(project) {
            pluginManager.apply(SpigradlePlugin::class)
            setupYamlGenTask()
            setupGroovyExtensions()
        }
    }

    private fun Project.setupYamlGenTask() {
        val description = extensions.create<SpigotPluginDescription>(EXTENSION_NAME, this)
        val generateTask = tasks.create(YAML_GEN_TASK_NAME, GenerateYamlTask::class) {
            doFirst {
                description.setDefaults(this@setupYamlGenTask)
                validateDescription(description)
                setToOptionMap(description)
            }
        }
        Groovies.getExtensionFrom(description).setSpigotExtension()
        val processResources: ProcessResources by tasks
        processResources.from(generateTask)
    }

    private fun SpigotPluginDescription.setDefaults(project: Project) {
        main = main ?: project.spigotPluginMain
        name = name ?: project.name
        version = version ?: project.version.toString()
    }

    private fun validateDescription(description: SpigotPluginDescription) {
        notNull(description.main) { Messages.noMainFound(EXTENSION_NAME, YAML_GEN_TASK_NAME) }
    }

    private val Project.spigotPluginMain: String?
        get() = runCatching { // get APT result or find out
            val file = File(project.buildDir, PLUGIN_APT_DEFAULT_PATH)
            file.readText()
        }.getOrNull() ?: findSpigotPluginMain()

    private fun Project.findSpigotPluginMain(): String? {
        tasks.withType<AbstractCompile>().map { it.destinationDir }.forEach {
            println(it.absolutePath) // TODO
        }
        return null
    }

    private fun Project.setupGroovyExtensions() {
        setupRepositories()
        setupDependencies()
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