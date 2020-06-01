package kr.entree.spigradle.module.spigot

import com.fasterxml.jackson.module.kotlin.readValue
import kr.entree.spigradle.data.FileEntry
import kr.entree.spigradle.data.Load
import kr.entree.spigradle.data.SpigotRepositories
import kr.entree.spigradle.internal.Groovies
import kr.entree.spigradle.module.common.Download
import kr.entree.spigradle.module.common.SpigradlePlugin
import kr.entree.spigradle.module.common.applySpigradlePlugin
import kr.entree.spigradle.module.common.setupDescGenTask
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.Delete
import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.*
import java.io.File

/**
 * Created by JunHyung Lim on 2020-04-28
 */
class SpigotPlugin : Plugin<Project> {
    companion object {
        const val DESC_GEN_TASK_NAME = "generateSpigotDescription"
        const val MAIN_DETECTION_TASK_NAME = "detectSpigotMain"
        const val EXTENSION_NAME = "spigot"
        const val DESC_FILE_NAME = "plugin.yml"
        const val PLUGIN_SUPER_CLASS = "org/bukkit/plugin/java/JavaPlugin"
        const val BUILD_TOOLS_URL = "https://hub.spigotmc.org/jenkins/job/BuildTools/lastSuccessfulBuild/artifact/target/BuildTools.jar"
    }

    override fun apply(project: Project) {
        with(project) {
            applySpigradlePlugin()
            setupDefaultRepositories()
            setupDescGenTask<SpigotDescription>(
                    EXTENSION_NAME,
                    DESC_GEN_TASK_NAME,
                    MAIN_DETECTION_TASK_NAME,
                    DESC_FILE_NAME,
                    PLUGIN_SUPER_CLASS
            )
            setupGroovyExtensions()
            setupSpigotDebugTasks()
        }
    }

    private fun Project.setupDefaultRepositories() {
        SpigotRepositories.run {
            listOf(SPIGOT_MC, PAPER_MC)
        }.forEach {
            repositories.maven(it)
        }
    }

    private fun Project.setupGroovyExtensions() {
        Groovies.getExtensionFrom(extensions.getByName(EXTENSION_NAME)).apply {
            set("POST_WORLD", Load.POST_WORLD)
            set("STARTUP", Load.STARTUP)
        }
    }

    private fun Project.setupSpigotDebugTasks() {
        // downloadBuildTools -> buildSpigot -> copySpigot -> runSpigot -> copyPlugins(TO-DO)
        // prepareSpigot, runSpigot
        val debugOption = extensions.getByName<SpigotDescription>("spigot").debug
        // Spigot
        val buildToolDownload = createDownloadBuildTool(debugOption)
        val buildSpigot = createBuildSpigot(debugOption).apply {
            mustRunAfter(buildToolDownload)
        }
        val prepareSpigot = createPrepareSpigot(debugOption).apply {
            dependsOn(buildToolDownload, buildSpigot)
        }
        val runSpigot = createRunServer("Spigot", debugOption.spigotFile, debugOption).apply {
            dependsOn(prepareSpigot)
        }
        val build by tasks
        val preparePluginSpigot = createPreparePlugin("Spigot", debugOption.spigotFile).apply {
            dependsOn(build)
        }
        createDebugSpigot().apply {
            dependsOn(preparePluginSpigot, runSpigot)
            runSpigot.mustRunAfter(preparePluginSpigot)
        }
        createCleanSpigotBuild(debugOption)
        // Paper
        val paperClipDownload = createDownloadPaper(debugOption)
        val preparePaper = createPreparePaper(debugOption).apply {
            dependsOn(paperClipDownload)
        }
        val preparePluginPaper = createPreparePlugin("Paper", debugOption.paperFile).apply {
            dependsOn(build)
        }
        val runPaper = createRunServer("Paper", debugOption.paperFile, debugOption).apply {
            dependsOn(preparePaper)
        }
        createDebugPaper().apply {
            dependsOn(preparePluginPaper, runPaper)
        }
    }

    private fun Project.createDownloadBuildTool(debugOption: SpigotDebug): Download {
        return tasks.create("downloadSpigotBuildTools", Download::class).apply {
            description = "Download the BuildTools."
            source = BUILD_TOOLS_URL
            afterEvaluate {
                destination = debugOption.buildToolFile.file
            }
        }
    }

    private fun Project.createCleanSpigotBuild(debugOption: SpigotDebug): Delete {
        return tasks.create("cleanSpigotBuild", Delete::class).apply {
            group = "spigradle"
            description = "Delete all BuildTools directories"
            delete(debugOption.buildToolFile.directory)
            delete(debugOption.buildToolOutputDirectory)
        }
    }

    private fun Project.createBuildSpigot(debugOption: SpigotDebug): JavaExec {
        return tasks.create("buildSpigot", JavaExec::class) {
            group = "spigradle"
            description = "Build the spigot.jar using the BuildTools."
            outputs.cacheIf { true }
            afterEvaluate {
                val (buildToolJar, buildToolDirectory) = debugOption.buildToolFile
                outputs.dir(File(buildToolDirectory, "CraftBukkit/target/classes"))
                classpath = files(buildToolJar)
                workingDir = buildToolDirectory
                args(
                        "--rev", debugOption.buildVersion,
                        "--output-dir", debugOption.buildToolOutputDirectory
                )
            }
        }
    }

    private fun Project.createPrepareSpigot(debugOption: SpigotDebug): Task {
        return tasks.create("prepareSpigot", Copy::class) {
            group = "spigradle"
            description = "Copy the spigot.jar generated by BuildTools into the given path."
            afterEvaluate {
                val (spigotJar, spigotDir) = debugOption.spigotFile
                val buildResultJar = runCatching {
                    debugOption.buildToolFile.directory.findBuildResultJar()
                }.getOrElse {
                    throw GradleException("Error while reading buildVersion in build info.json.", it)
                }
                from(buildResultJar)
                into(spigotDir)
                rename { spigotJar.name }
            }
        }
    }

    private fun File.readBuildVersion() = SpigradlePlugin.mapper.readValue<Map<String, Any>>(
            File(this, "BuildData/info.json")
    )["minecraftVersion"]?.toString()

    private fun File.findBuildResultJar() = File(this, "spigot-${readBuildVersion()}.jar")

    private fun Project.createRunServer(name: String, workFile: FileEntry, debug: SpigotDebug): JavaExec {
        val (serverJar, serverDir) = workFile
        return tasks.create("run$name", JavaExec::class) {
            group = "spigradle"
            description = "Startup the $name server."
            standardInput = System.`in`
            args("nogui")
            doFirst {
                if (!debug.eula) {
                    throw GradleException("""
                        Please set the 'eula' property to true if you agree the Mojang EULA.
                        https://account.mojang.com/documents/minecraft_eula
                    """.trimIndent())
                }
                classpath = files(serverJar)
                workingDir = serverDir
                File(serverDir, "eula.txt").writeText("eula=true")
            }
        }
    }

    private fun Project.createPreparePlugin(name: String, entry: FileEntry): Task {
        return tasks.create("preparePlugin$name", Copy::class) {
            group = "spigradle"
            description = "Copy the jars into the $name server."
            afterEvaluate {
                val pluginJar = tasks.withType<Jar>().asSequence().mapNotNull {
                    it.archiveFile.orNull?.asFile
                }.find {
                    it.isFile
                } ?: throw GradleException("Couldn't find a plugin.jar")
                from(pluginJar)
                into(File(entry.directory, "plugins"))
            }
        }
    }

    private fun Project.createDebugSpigot(): Task {
        return tasks.create("debugSpigot") {
            group = "spigradle"
            description = "Startup the spigot server with your plugin.jar"
        }
    }

    private fun Project.createDebugPaper(): Task {
        return tasks.create("debugPaper") {
            group = "spigradle"
            description = "Startup the paper server with your plugin.jar"
        }
    }

    private fun Project.createDownloadPaper(debug: SpigotDebug): Task {
        return tasks.create("downloadPaper", Download::class).apply {
            description = "Download the PaperClip."
            afterEvaluate {
                source = source ?: "https://papermc.io/api/v1/paper/${debug.buildVersion}/latest/download"
                destination = debug.paperFile.file
            }
        }
    }

    private fun Project.createPreparePaper(debug: SpigotDebug): Task {
        return tasks.create("preparePaper").apply {
            group = "spigradle"
            description = "Download Paperclip for ready to run."
        }
    }
}