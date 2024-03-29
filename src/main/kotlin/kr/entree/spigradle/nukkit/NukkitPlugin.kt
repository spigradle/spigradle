/*
 * Copyright (c) 2023 Spigradle contributors.
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

package kr.entree.spigradle.nukkit

import kr.entree.spigradle.*
import kr.entree.spigradle.annotations.PluginType
import kr.entree.spigradle.spigot.Load
import kr.entree.spigradle.yaml.registerDescGenTask
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
        val NUKKIT_TYPE = PluginConvention(
                serverName = "nukkit",
                descFile = "plugin.yml",
                mainSuperClass = "cn/nukkit/plugin/PluginBase",
                mainType = PluginType.NUKKIT
        )
    }

    val Project.nukkit get() = extensions.getByName<NukkitExtension>(NUKKIT_TYPE.descExtension)

    override fun apply(project: Project) {
        with(project) {
            applySpigradlePlugin()
            setupDefaultRepositories()
            registerDescGenTask<NukkitExtension>(NUKKIT_TYPE)
            setupGroovyExtensions()
            setupNukkitDebugTasks()
            createRunConfigurations("Nukkit", nukkit.debug)
        }
    }

    private fun Project.setupDefaultRepositories() {
        repositories.maven(NukkitRepositories.NUKKIT_X)
    }

    private fun Project.setupGroovyExtensions() {
        extensions.getByName(NUKKIT_TYPE.descExtension).groovyExtension.apply {
            set("POST_WORLD", Load.POST_WORLD)
            set("POSTWORLD", Load.POST_WORLD)
            set("STARTUP", Load.STARTUP)
        }
    }

    private fun Project.setupNukkitDebugTasks() {
        val nukkit: NukkitExtension by extensions
        val debug = nukkit.debug
        val assemble by tasks
        with(NukkitDebugTask) {
            val downloadNukkit = registerDownloadNukkit(debug)
            val prepareNukkit = registerPrepareNukkit().applyToConfigure {
                dependsOn(downloadNukkit)
            }
            val runNukkit = registerRunNukkit(debug).applyToConfigure {
                mustRunAfter(prepareNukkit)
            }
            val preparePlugin = registerPrepareNukkitPlugins(nukkit).applyToConfigure {
                dependsOn(assemble)
            }
            registerDebugNukkit().applyToConfigure {
                dependsOn(preparePlugin, prepareNukkit, runNukkit)
            }
        }
    }
}