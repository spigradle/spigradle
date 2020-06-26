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

package kr.entree.spigradle.module.nukkit

import kr.entree.spigradle.data.Load
import kr.entree.spigradle.data.NukkitRepositories
import kr.entree.spigradle.internal.applyToConfigure
import kr.entree.spigradle.internal.groovyExtension
import kr.entree.spigradle.module.common.applySpigradlePlugin
import kr.entree.spigradle.module.common.createDebugConfigurations
import kr.entree.spigradle.module.common.registerDescGenTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByName
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.maven
import org.gradle.kotlin.dsl.provideDelegate

/**
 * The Nukkit plugin that adds:
 * - [kr.entree.spigradle.module.common.YamlGenerate] task for the 'plugin.yml' generation.
 * - [kr.entree.spigradle.module.common.SubclassDetection] task for the main-class detection.
 * - Debug tasks for test your plugin.
 */
class NukkitPlugin : Plugin<Project> {
    companion object {
        const val DESC_GEN_TASK_NAME = "generateNukkitDescription"
        const val MAIN_DETECTION_TASK_NAME = "detectNukkitMain"
        const val EXTENSION_NAME = "nukkit"
        const val DESC_FILE_NAME = "plugin.yml"
        const val PLUGIN_SUPER_CLASS = "cn/nukkit/plugin/PluginBase"
    }

    val Project.nukkit get() = extensions.getByName<NukkitExtension>(EXTENSION_NAME)

    override fun apply(project: Project) {
        with(project) {
            applySpigradlePlugin()
            setupDefaultRepositories()
            registerDescGenTask<NukkitExtension>(
                    EXTENSION_NAME,
                    DESC_GEN_TASK_NAME,
                    MAIN_DETECTION_TASK_NAME,
                    DESC_FILE_NAME,
                    PLUGIN_SUPER_CLASS
            )
            setupGroovyExtensions()
            setupNukkitDebugTasks()
            createDebugConfigurations("Nukkit", nukkit.debug)
        }
    }

    private fun Project.setupDefaultRepositories() {
        repositories.maven(NukkitRepositories.NUKKIT_X)
    }

    private fun Project.setupGroovyExtensions() {
        extensions.getByName(EXTENSION_NAME).groovyExtension.apply {
            set("POST_WORLD", Load.POST_WORLD)
            set("POSTWORLD", Load.POST_WORLD)
            set("STARTUP", Load.STARTUP)
        }
    }

    private fun Project.setupNukkitDebugTasks() {
        val nukkit: NukkitExtension by extensions
        val debug = nukkit.debug
        val build by tasks
        with(NukkitDebugTask) {
            val nukkitDownload = registerDownloadNukkit(debug)
            val runNukkit = registerRunNukkit(debug)
            val preparePlugin = registerPrepareNukkitPlugins(nukkit).applyToConfigure {
                dependsOn(build)
            }
            registerDebugNukkit().applyToConfigure {
                dependsOn(preparePlugin, nukkitDownload, runNukkit)
                runNukkit.get().mustRunAfter(nukkitDownload)
            }
        }
    }
}