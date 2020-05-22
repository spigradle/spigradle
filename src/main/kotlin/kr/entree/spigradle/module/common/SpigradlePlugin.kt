package kr.entree.spigradle.module.common

import groovy.lang.Closure
import kr.entree.spigradle.data.*
import kr.entree.spigradle.data.Repositories.SONATYPE
import kr.entree.spigradle.internal.Groovies
import kr.entree.spigradle.internal.PLUGIN_APT_DEFAULT_PATH
import kr.entree.spigradle.internal.PLUGIN_APT_RESULT_PATH_KEY
import kr.entree.spigradle.internal.toFieldEntries
import kr.entree.spigradle.module.common.task.GenerateYamlTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.internal.artifacts.dsl.DefaultRepositoryHandler
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.kotlin.dsl.*
import java.io.File

/**
 * Created by JunHyung Lim on 2020-05-18
 */
class SpigradlePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            setupPlugins()
            setupDefaultDependencies()
            setupGroovyExtensions()
            setupAnnotationProcessorOptions()
        }
    }

    @Suppress("UnstableApiUsage")
    private fun Project.setupPlugins() {
        pluginManager.apply(JavaPlugin::class)
        if (plugins.hasPlugin("org.jetbrains.kotlin.jvm")) {
            plugins.apply("org.jetbrains.kotlin.kapt")
            afterEvaluate {
                val kaptKotlin: Task by tasks
                tasks.withType(GenerateYamlTask::class) {
                    kaptKotlin.finalizedBy(this) // For proper task ordering
                }
            }
        }
    }

    private fun Project.setupDefaultDependencies() {
        dependencies.apply {
            val spigradleNotation = Dependencies.SPIGRADLE.format()
            add(JavaPlugin.COMPILE_ONLY_CONFIGURATION_NAME, spigradleNotation)
            if (project.configurations.findByName("kapt") != null) {
                add("kapt", spigradleNotation)
            } else {
                add(JavaPlugin.ANNOTATION_PROCESSOR_CONFIGURATION_NAME, spigradleNotation)
            }
        }
    }

    private fun Project.setupGroovyExtensions() {
        setupRepositoryExtensions()
        setupDependencyExtensions()
    }

    private fun Project.setupRepositoryExtensions() {
        val ext = Groovies.getExtensionFrom(repositories)
        listOf(Repositories, SpigotRepositories).flatMap {
            it.toFieldEntries<String>()
        }.forEach { (name, url) ->
            ext.set(name, object : Closure<Any>(this, this) {
                fun doCall() = repositories.maven(url)
            })
        }
        SpigotRepositories.run {
            listOf(SPIGOT_MC, SONATYPE, PAPER_MC, DefaultRepositoryHandler.BINTRAY_JCENTER_URL)
        }.forEach {
            repositories.maven(it)
        }
    }

    private fun Project.setupDependencyExtensions() {
        val ext = Groovies.getExtensionFrom(dependencies)
        listOf(Dependencies, SpigotDependencies).flatMap {
            it.toFieldEntries<Dependency>()
        }.forEach { (name, dependency) ->
            ext.set(name, object : Closure<Any>(this, this) {
                fun doCall(version: String?) = dependency.format(version)
            })
        }
    }

    private fun Project.setupAnnotationProcessorOptions() {
        val compileJava: JavaCompile by tasks
        val path = File(buildDir, PLUGIN_APT_DEFAULT_PATH)
        compileJava.options.compilerArgs.add("-A${PLUGIN_APT_RESULT_PATH_KEY}=${path.absolutePath}")
    }
}