package kr.entree.spigradle.module.common

import kr.entree.spigradle.data.Dependencies
import kr.entree.spigradle.internal.PLUGIN_APT_DEFAULT_PATH
import kr.entree.spigradle.internal.PLUGIN_APT_RESULT_PATH_KEY
import kr.entree.spigradle.module.common.task.GenerateYamlTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.withType
import java.io.File

/**
 * Created by JunHyung Lim on 2020-05-18
 */
class SpigradlePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            setupPlugins()
            setupDependencies()
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

    @Suppress("UnstableApiUsage")
    private fun Project.setupDependencies() {
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

    private fun Project.setupAnnotationProcessorOptions() {
        val compileJava: JavaCompile by tasks
        val path = File(buildDir, PLUGIN_APT_DEFAULT_PATH)
        compileJava.options.compilerArgs.add("-A${PLUGIN_APT_RESULT_PATH_KEY}=${path.absolutePath}")
    }
}