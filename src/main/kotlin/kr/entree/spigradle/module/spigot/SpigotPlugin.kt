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
import kr.entree.spigradle.module.common.task.SubclassDetectionTask
import kr.entree.spigradle.module.spigot.data.SpigotDependencies
import kr.entree.spigradle.module.spigot.data.SpigotRepositories
import kr.entree.spigradle.module.spigot.data.setSpigotExtension
import kr.entree.spigradle.module.spigot.extension.SpigotPluginDescription
import notNull
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.internal.artifacts.dsl.DefaultRepositoryHandler.BINTRAY_JCENTER_URL
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.kotlin.dsl.*
import java.io.File

/**
 * Created by JunHyung Lim on 2020-04-28
 */
@Suppress("UnstableApiUsage")
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
            setupGroovyExtensions()
        }
    }

    private fun Project.setupYamlGenTask() {
        val description = extensions.create<SpigotPluginDescription>(EXTENSION_NAME, this)
        val detectionTask = tasks.create(MAIN_DETECTION_TASK_NAME, SubclassDetectionTask::class, BUKKIT_PLUGIN_SUPER_CLASS).apply {
            afterEvaluate {
                classDirectories = convention.getPlugin(JavaPluginConvention::class).run {
                    sourceSets["main"].output.classesDirs
                }
            }
        }
        val generateTask = tasks.create(YAML_GEN_TASK_NAME, GenerateYamlTask::class) {
            inputs.files(detectionTask.outputFile)
            doFirst {
                description.setDefaults(this@setupYamlGenTask)
                validateDescription(description)
                setToOptionMap(description)
            }
            afterEvaluate {
                val resourceDir = withConvention(JavaPluginConvention::class) {
                    sourceSets["main"].output.resourcesDir
                } ?: return@afterEvaluate
                outputFile = File(resourceDir, "plugin.yml")
            }
        }
        Groovies.getExtensionFrom(description).setSpigotExtension()
        val classes: Task by tasks
        classes.finalizedBy(generateTask)
        generateTask.dependsOn(detectionTask) // classes -> detectionTask -> generateTask
    }

    private fun SpigotPluginDescription.setDefaults(project: Project) {
        main = main ?: project.mainDetectionResult
        name = name ?: project.name
        version = version ?: project.version.toString()
    }

    private fun validateDescription(description: SpigotPluginDescription) {
        notNull(description.main) { Messages.noMainFound(EXTENSION_NAME, YAML_GEN_TASK_NAME) }
    }

    private val Project.mainDetectionResult: String?
        get() = runCatching {
            val file = File(project.buildDir, PLUGIN_APT_DEFAULT_PATH)
            file.readText()
        }.getOrNull()

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