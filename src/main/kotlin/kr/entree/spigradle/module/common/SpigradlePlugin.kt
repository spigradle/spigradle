/*
 * Copyright (c) 2020 Spigradle contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kr.entree.spigradle.module.common

import groovy.lang.Closure
import kr.entree.spigradle.data.*
import kr.entree.spigradle.data.Repositories.SONATYPE
import kr.entree.spigradle.internal.Groovies
import kr.entree.spigradle.internal.PLUGIN_APT_DEFAULT_PATH
import kr.entree.spigradle.internal.PLUGIN_APT_RESULT_PATH_KEY
import kr.entree.spigradle.internal.toFieldEntries
import kr.entree.spigradle.kotlin.mockBukkit
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.internal.artifacts.dsl.DefaultRepositoryHandler.BINTRAY_JCENTER_URL
import org.gradle.api.invocation.Gradle
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.tasks.Delete
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.kotlin.dsl.*
import org.gradle.plugins.ide.idea.IdeaPlugin
import org.gradle.plugins.ide.idea.model.IdeaModel
import org.jetbrains.gradle.ext.IdeaExtPlugin
import java.io.File
import kotlin.reflect.full.declaredMemberExtensionFunctions
import kotlin.reflect.full.memberExtensionFunctions

/**
 * Created by JunHyung Lim on 2020-05-18
 */
fun Project.applySpigradlePlugin() = pluginManager.apply(SpigradlePlugin::class)

val Gradle.spigotBuildToolDir get() = File(gradleUserHomeDir, "spigot-buildtools")

val Project.debugDir get() = File(projectDir, "debug")

class SpigradlePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            setupPlugins()
            setupDefaultDependencies()
            setupDefaultRepositories()
            setupGroovyExtensions()
            setupAnnotationProcessorOptions()
            markExcludeDirectories()
            setupTasks()
        }
    }

    @Suppress("UnstableApiUsage")
    private fun Project.setupPlugins() {
        pluginManager.apply(JavaPlugin::class)
        pluginManager.apply(IdeaPlugin::class)
        pluginManager.apply(IdeaExtPlugin::class)
        if (plugins.hasPlugin("org.jetbrains.kotlin.jvm")) {
            plugins.apply("org.jetbrains.kotlin.kapt")
            afterEvaluate {
                val kaptKotlin: Task by tasks
                tasks.withType(YamlGenerate::class) {
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

    private fun Project.setupDefaultRepositories() {
        repositories.gradlePluginPortal() // For avoid APT errors
    }

    private fun Project.setupGroovyExtensions() {
        setupRepositoryExtensions()
        setupDependencyExtensions()
    }

    private fun Project.setupRepositoryExtensions() {
        val ext = Groovies.getExtensionFrom(repositories)
        listOf(
                Repositories, SpigotRepositories,
                BungeeRepositories, NukkitRepositories
        ).flatMap {
            it.toFieldEntries<String>()
        }.forEach { (name, url) ->
            ext.set(name, object : Closure<Any>(this, this) {
                fun doCall() = repositories.maven(url)
            })
        }
        listOf(SONATYPE, BINTRAY_JCENTER_URL).forEach {
            repositories.maven(it)
        }
    }

    private fun Project.setupDependencyExtensions() {
        val ext = Groovies.getExtensionFrom(dependencies)
        listOf(
                Dependencies, SpigotDependencies,
                BungeeDependencies, NukkitDependencies
        ).flatMap {
            it.toFieldEntries<Dependency>()
        }.forEach { (name, dependency) ->
            ext.set(name, object : Closure<Any>(this, this) {
                fun doCall(version: String?) = dependency.format(version)
            })
        }
        ext.set("mockBukkit", object: Closure<Any>(this, this) {
            fun doCall(spigotVersion: String? = null, mockBukkitVersion: String? = null) =
                    dependencies.mockBukkit(spigotVersion, mockBukkitVersion)
        }) // Can be replaced by reflection to SpigotExtensionsKt
    }

    private fun Project.setupAnnotationProcessorOptions() {
        val compileJava: JavaCompile by tasks
        val path = File(buildDir, PLUGIN_APT_DEFAULT_PATH)
        compileJava.options.compilerArgs.add("-A${PLUGIN_APT_RESULT_PATH_KEY}=${path.absolutePath}")
    }

    private fun Project.markExcludeDirectories() {
        val idea: IdeaModel by extensions
        // Mark exclude directories
        idea.module {
            excludeDirs = setOf(debugDir) + excludeDirs
        }
    }

    private fun Project.setupTasks() {
        tasks.register("cleanDebug", Delete::class) {
            group = "spigradle"
            description = "Delete the debug directory."
            delete(debugDir)
        }
    }
}