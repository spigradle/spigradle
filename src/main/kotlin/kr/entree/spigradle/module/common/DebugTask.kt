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

import com.fasterxml.jackson.module.kotlin.readValue
import kr.entree.spigradle.internal.*
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.logging.LogLevel
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.*
import org.gradle.kotlin.dsl.support.useToRun
import org.gradle.plugins.ide.idea.model.IdeaModel
import org.jetbrains.gradle.ext.GradleTask
import org.jetbrains.gradle.ext.JarApplication
import org.jetbrains.gradle.ext.Remote
import java.io.File
import java.util.jar.JarFile

/**
 * Created by JunHyung Lim on 2020-06-07
 */
internal fun File.readYamlDescription(fileName: String) =
        runCatching {
            JarFile(this).run {
                getEntry(fileName)?.run {
                    getInputStream(this)
                }
            }?.useToRun {
                Jackson.YAML.readValue<Map<String, Any>>(this)
            }
        }.getOrNull()

internal fun Project.createRunConfigurations(name: String, debug: CommonDebug) {
    val idea: IdeaModel by extensions
    idea.project?.settings {
        runConfigurations {
            register("Remote$name", Remote::class) {
                host = "localhost"
                port = debug.agentPort
            }
            register("Run$name", JarApplication::class) {
                jarPath = debug.serverJar.absolutePath
                workingDirectory = debug.serverDirectory.absolutePath
                programParameters = debug.args.joinToString(" ")
                jvmArgs = debug.jvmArgs.joinToString(" ")
                beforeRun {
                    register("prepareServer", GradleTask::class) {
                        task = tasks.getByName("prepare$name")
                    }
                    register("preparePlugins", GradleTask::class) {
                        task = tasks.getByName("prepare${name}Plugins")
                    }
                }
            }
        }
    }
}

object DebugTask {
    fun Project.registerRunServer(serverName: String, debug: CommonDebug): TaskProvider<JavaExec> {
        return tasks.register("run$serverName", JavaExec::class) {
            standardInput = System.`in`
            logging.captureStandardOutput(LogLevel.LIFECYCLE)
            jvmArgs(lazyString { "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=${debug.agentPort}" })
            jvmArgs(debug.jvmArgs)
            args(debug.args)
            doFirst {
                if (!debug.serverJar.isFile) {
                    throw GradleException("""
                        Cannot find the server jar at the given path: ${debug.serverJar.absolutePath}
                        Did you run 'debug$serverName' task instead of 'run$serverName'?
                    """.trimIndent())
                }
            }
        }
    }

    fun Project.registerPreparePlugins(
            name: String,
            nameProperty: String,
            descFileName: String,
            dependPlugins: () -> Iterable<String>
    ): TaskProvider<Copy> {
        return tasks.register(name, Copy::class) {
            description = "Copy the plugin jars"
            from(provider { tasks.findArtifactJar() ?: files() })
            from(cachingProvider {
                val needPlugins = dependPlugins().toMutableSet()
                val readyPlugins = mutableSetOf<String>()
                // Remove already had plugins
                destinationDir.listFiles { _, name ->
                    name.endsWith(".jar")
                }?.asSequence()?.mapNotNull {
                    it.readYamlDescription(descFileName)?.get(nameProperty)?.toString()
                }?.takeWhile {
                    !readyPlugins.containsAll(needPlugins)
                }?.forEach { readyPlugins += it }
                // Find depend plugins from classpath
                project.withConvention(JavaPluginConvention::class) {
                    sourceSets["main"].compileClasspath
                }.asSequence().mapNotNull { depFile ->
                    depFile.readYamlDescription(descFileName)
                            ?.let { desc -> depFile to desc }
                }.takeWhile {
                    !readyPlugins.containsAll(needPlugins)
                }.filter { (_, desc) ->
                    val pluginName = desc["name"]?.toString()
                    pluginName != null && pluginName in needPlugins
                            && readyPlugins.add(pluginName)
                }.mapNotNull { it.first }.toList().apply {
                    (needPlugins - readyPlugins).forEach {
                        logger.error("Unable to resolve the plugin dependency: $it")
                    }
                }
            })
        }
    }
}