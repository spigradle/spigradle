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

@file:Suppress("UnstableApiUsage")

package kr.entree.spigradle.module.spigot

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import de.undercouch.gradle.tasks.download.Download
import kr.entree.spigradle.data.SpigotDebug
import kr.entree.spigradle.internal.Jackson
import kr.entree.spigradle.internal.applyToConfigure
import kr.entree.spigradle.internal.lazyString
import kr.entree.spigradle.module.common.DebugTask.registerPreparePlugins
import kr.entree.spigradle.module.common.DebugTask.registerRunServer

import kr.entree.spigradle.module.common.YamlGenerate
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.logging.LogLevel
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.Delete
import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.register
import java.io.File
import java.util.concurrent.atomic.AtomicReference

/**
 * Created by JunHyung Lim on 2020-06-03
 */
object SpigotDebugTask {
    const val BUILD_TOOLS_URL =
        "https://hub.spigotmc.org/jenkins/job/BuildTools/lastSuccessfulBuild/artifact/target/BuildTools.jar"
    val TASK_GROUP_DEBUG = "${SpigotPlugin.SPIGOT_TYPE.taskGroup} debug"
    val DEFAULT_SPIGOT_BUILD_VERSION = "1.18.1"
    val spigotGroups = mapOf(
        "org.spigotmc" to setOf("spigot-api", "spigot"),
        "com.destroystokyo.paper" to setOf("paper-api"),
        "io.papermc.paper" to setOf("paper-api")
    )
    val listComparator =
        Comparator<List<Int>> { a, b ->
            a.withIndex().map { (i, v) ->
                (b.getOrNull(i) ?: 0).compareTo(v)
            }.firstOrNull { it != 0 } ?: 0
        }

    val paperBuildsJsonFilename: String = "paper-builds.json"
    val paperDownloadsJsonFilename: String = "paper-downloads.json"
    val getPaperBuildsTaskname: String = "getPaperBuilds"
    val getPaperDownloadsTaskname: String = "getPaperDownloads"
    val downloadPaperTaskname: String = "downloadPaper"

    fun Project.registerDownloadBuildTool(debugOption: SpigotDebug): TaskProvider<Download> {
        return tasks.register("downloadSpigotBuildTools", Download::class) {
            group = TASK_GROUP_DEBUG
            description = "Download the BuildTools."
            src(BUILD_TOOLS_URL)
            dest(provider { debugOption.buildToolJar })
        }
    }

    fun Project.registerCleanSpigotBuild(debugOption: SpigotDebug): TaskProvider<Delete> {
        return tasks.register("cleanSpigotBuild", Delete::class) {
            group = TASK_GROUP_DEBUG
            description = "Delete all BuildTools directories."
            delete(debugOption.buildToolDirectory)
            delete(debugOption.buildToolOutputDirectory)
        }
    }

    fun Project.registerBuildSpigot(options: SpigotDebug): TaskProvider<JavaExec> {
        return tasks.register("buildSpigot", JavaExec::class) {
            group = TASK_GROUP_DEBUG
            description = "Build the Spigot using the BuildTools."
            minHeapSize = "512M"
            classpath = files(provider { options.buildToolJar })
            outputs.cacheIf { true }
            outputs.dir(provider { File(options.buildToolDirectory, "CraftBukkit/target/classes") })
            logging.captureStandardOutput(LogLevel.DEBUG)
            setWorkingDir(provider { options.buildToolDirectory })
            args(
                "--rev", lazyString { getBuildVersion(options) },
                "--output-dir", lazyString { options.buildToolOutputDirectory.absolutePath }
            )
            doFirst {
                // Remove empty directory created by gradle for avoid buildtools failure
                val classesDir = File(options.buildToolDirectory, "CraftBukkit/target/classes")
                val craftbukkitDir = File(options.buildToolDirectory, "CraftBukkit")
                if (classesDir.listFiles()?.isNotEmpty() != true) {
                    craftbukkitDir.deleteRecursively()
                }
                logger.lifecycle("It may take a while. To see build logs, start with the option '-d' or '--debug'.")
            }
        }
    }

    // TODO: Need to change `Copy` to just `Task`
    fun Project.registerPrepareSpigot(options: SpigotDebug): TaskProvider<Copy> {
        return tasks.register("prepareSpigot", Copy::class) {
            group = TASK_GROUP_DEBUG
            description = "Copy the Spigot generated by BuildTools into the given path."
            from(provider { File(options.buildToolDirectory, "Spigot/Spigot-Server/target") }) {
                include("spigot*.jar")
            }
            into(provider { options.serverDirectory })
            rename { options.serverJar.name }
        }
    }

    fun Project.registerAcceptEula(debug: SpigotDebug): TaskProvider<Task> {
        return tasks.register("acceptSpigotEula") {
            group = TASK_GROUP_DEBUG
            description = "Accepts Mojang EULA."
            doLast {
                ensureMinecraftEULA(debug.serverDirectory, debug.eula)
            }
        }
    }

    fun Project.registerRunSpigot(debug: SpigotDebug): TaskProvider<JavaExec> {
        val serverJar = debug.serverJar
        return registerRunServer("Spigot", debug).applyToConfigure {
            group = TASK_GROUP_DEBUG
            description = "Startup the Spigot server."
            classpath = files(provider { serverJar })
            if (debug.serverPort >= 0) {
                args("--port", lazyString { debug.serverPort })
            }
            setWorkingDir(provider { debug.serverDirectory })
        }
    }

    fun Project.registerPrepareSpigotPlugin(spigot: SpigotExtension): TaskProvider<Copy> {
        return registerPreparePlugins(
            "prepareSpigotPlugins",
            "plugin.yml",
            provider { spigot.depends + spigot.softDepends }
        ).applyToConfigure {
            group = TASK_GROUP_DEBUG
            outputs.files(fileTree(spigot.debug.serverDirectory.resolve("plugins")) {
                include("*.jar")
            })
            into(provider { File(spigot.debug.serverDirectory, "plugins") })
        }
    }

    fun Project.registerDebugRun(name: String): TaskProvider<Task> {
        return tasks.register("debug$name") {
            group = TASK_GROUP_DEBUG
            description = "Startup the $name server with your plugin.jar."
        }
    }

    fun Project.registerGetPaperBuilds(version: Provider<String>): TaskProvider<Download> {
        return tasks.register(getPaperBuildsTaskname, Download::class) {
            src(provider { "https://papermc.io/api/v2/projects/paper/versions/${version.get()}" })
            dest(temporaryDir.resolve(paperBuildsJsonFilename))
        }
    }

    fun parseLatestPaperBuildsJson(json: JsonNode): Long? =
        json.get("builds").elements().asSequence().map { it.longValue() }.max()

    fun Project.registerGetPaperDownloads(build: Provider<Long>, version: Provider<String>): TaskProvider<Download> {
        return tasks.register(getPaperDownloadsTaskname, Download::class) {
            src(provider {
                "https://papermc.io/api/v2/projects/paper/versions/${version.get()}/builds/${build.get()}"
            })
            dest(temporaryDir.resolve(paperDownloadsJsonFilename))
            dependsOn(getPaperBuildsTaskname)
        }
    }

    fun parsePaperDownloadsNameJson(json: JsonNode): String? =
        json.get("downloads")?.get("application")?.get("name")?.textValue()

    fun Project.registerDownloadPaper(debug: SpigotDebug): TaskProvider<Download> {
        val buildR = AtomicReference<Long>()
        val filenameR = AtomicReference<String>()

        val getPaperBuildsTask = registerGetPaperBuilds(provider { debug.getBuildVersionOrDefault() })
        getPaperBuildsTask.configure {
            doLast {
                val jsonFile = getPaperBuildsTask.get().temporaryDir.resolve(paperBuildsJsonFilename)
                val json = ObjectMapper().readTree(jsonFile)
                val latest = parseLatestPaperBuildsJson(json)
                    ?: throw GradleException("Wrong json while get paper builds: ${json.toPrettyString()}")
                buildR.set(latest)
            }
        }
        val getPaperDownloadsTask =
            registerGetPaperDownloads(provider { buildR.get() }, provider { debug.getBuildVersionOrDefault() })
        getPaperDownloadsTask.configure {
            doLast {
                val jsonFile = getPaperDownloadsTask.get().temporaryDir.resolve(paperDownloadsJsonFilename)
                val json = ObjectMapper().readTree(jsonFile)
                val filename = parsePaperDownloadsNameJson(json)
                    ?: throw GradleException("Wrong json while get paper downloads: ${json.toPrettyString()}")
                filenameR.set(filename)
            }
        }
        val downloadPaperTask = tasks.register(downloadPaperTaskname, Download::class) {
            group = TASK_GROUP_DEBUG
            description = "Download the Paperclip."
            src(provider {
                "https://papermc.io/api/v2/projects/paper/versions/${debug.getBuildVersionOrDefault()}/builds/${buildR.get()}/downloads/${filenameR.get()}"
            })
            dest(provider { debug.serverJar })
            dependsOn(getPaperBuildsTask, getPaperDownloadsTask)
        }
        return downloadPaperTask
    }

    fun Project.registerSpigotConfiguration(serverDir: File): TaskProvider<YamlGenerate> {
        val spigotConfigFile = serverDir.resolve("spigot.yml")
        return tasks.register("configSpigot", YamlGenerate::class) {
            group = TASK_GROUP_DEBUG
            description = "Configuration for the spigot.yml"
            outputFiles.from(spigotConfigFile)
            properties.set(provider {
                val fileYaml = runCatching {
                    Jackson.YAML.readValue<Map<String, Any>>(spigotConfigFile)
                }.getOrNull() ?: emptyMap()
                fileYaml + ("settings" to mapOf(
                    "restart-on-crash" to false
                ))
            })
        }
    }

    fun Project.getBuildVersion(debug: SpigotDebug): String {
        return debug.buildVersion.ifEmpty {
            sortDescendingVersion(configurations.flatMap { cfg ->
                cfg.dependencies.filter {
                    (spigotGroups[it.group] ?: emptySet()).contains(it.name)
                }.mapNotNull {
                    it.version
                }
            }).firstOrNull() ?: DEFAULT_SPIGOT_BUILD_VERSION
        }
    }

    fun sortDescendingVersion(vers: List<String>): List<String> {
        return vers.sortedWith(mapComparator(listComparator) { s ->
            s.split(".").map {
                val pos = it.indexOf("-")
                if (pos >= 0) {
                    it.substring(0, pos)
                } else it
            }.map {
                it.toIntOrNull() ?: 0
            }
        })
    }

    fun <A, B> mapComparator(c: Comparator<A>, f: (B) -> A): Comparator<B> =
        Comparator { a, b -> c.compare(f(a), f(b)) }
}
