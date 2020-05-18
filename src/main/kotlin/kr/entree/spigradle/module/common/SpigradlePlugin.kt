package kr.entree.spigradle.module.common

import kr.entree.spigradle.data.Dependencies
import kr.entree.spigradle.internal.*
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile
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

    private fun Project.setupPlugins() {
        if (plugins.hasPlugin("org.jetbrains.kotlin.jvm")) {
            TODO("afterEvalute?")
        }
    }

    private fun Project.setupDependencies() {
        dependencies.apply {
            val spigradleNotation = Dependencies.SPIGRADLE.format()
            compileOnly(spigradleNotation)
            if (project.configurations.findByName("kapt") != null) {
                kapt(spigradleNotation)
            } else {
                annotationProcessor(spigradleNotation)
            }
        }
    }

    private fun Project.setupAnnotationProcessorOptions() {
        tasks.findByBoth<JavaCompile>("compileJava") {
            val path = File(buildDir, PLUGIN_APT_DEFAULT_PATH)
            options.compilerArgs.add("-A${PLUGIN_APT_RESULT_PATH_KEY}=${path.absolutePath}")
        }
    }
}