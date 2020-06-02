package kr.entree.spigradle.module.spigot

import com.fasterxml.jackson.module.kotlin.readValue
import kr.entree.spigradle.data.FileEntry
import kr.entree.spigradle.data.Load
import kr.entree.spigradle.data.SpigotRepositories
import kr.entree.spigradle.internal.Groovies
import kr.entree.spigradle.internal.readPluginJar
import kr.entree.spigradle.module.common.Download
import kr.entree.spigradle.module.common.SpigradlePlugin
import kr.entree.spigradle.module.common.applySpigradlePlugin
import kr.entree.spigradle.module.common.setupDescGenTask
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.plugins.JavaPluginConvention
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
        val DEBUG_TASK_SPIGOT_GROUP = debugTaskGroup("Spigot")
        val DEBUG_TASK_PAPER_GROUP = debugTaskGroup("Paper")

        fun debugTaskGroup(serverName: String) = "spigradle ${serverName.toLowerCase()} debug"
    }

    override fun apply(project: Project) {
        with(project) {
            applySpigradlePlugin()
            setupDefaultRepositories()
            setupDescGenTask<SpigotExtension>(
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
        val spigot: SpigotExtension by extensions
        val debugOption = spigot.debug
        // Spigot
        val buildToolDownload = createDownloadBuildTool(debugOption)
        val buildSpigot = createBuildSpigot(debugOption).apply {
            mustRunAfter(buildToolDownload)
        }
        val prepareSpigot = createPrepareSpigot(debugOption).apply {
            dependsOn(buildToolDownload, buildSpigot)
        }
        val runSpigot = createRunServer("Spigot", debugOption.spigotFile, debugOption).apply {
            group = DEBUG_TASK_SPIGOT_GROUP
            dependsOn(prepareSpigot)
        }
        val build by tasks
        val preparePluginSpigot = createPreparePlugin("Spigot", debugOption.spigotFile, spigot).apply {
            group = DEBUG_TASK_SPIGOT_GROUP
            dependsOn(build)
        }
        createDebugRun("Spigot").apply {
            group = DEBUG_TASK_SPIGOT_GROUP
            dependsOn(preparePluginSpigot, runSpigot)
            runSpigot.mustRunAfter(preparePluginSpigot)
        }
        createCleanSpigotBuild(debugOption)
        // Paper
        val paperClipDownload = createDownloadPaper(debugOption)
        val preparePaper = createPreparePaper(debugOption).apply {
            dependsOn(paperClipDownload)
        }
        val preparePluginPaper = createPreparePlugin("Paper", debugOption.paperFile, spigot).apply {
            group = DEBUG_TASK_PAPER_GROUP
            dependsOn(build)
        }
        val runPaper = createRunServer("Paper", debugOption.paperFile, debugOption).apply {
            dependsOn(preparePaper)
        }
        createDebugRun("Paper").apply {
            group = DEBUG_TASK_PAPER_GROUP
            dependsOn(preparePluginPaper, runPaper)
            runPaper.mustRunAfter(preparePluginPaper)
        }
    }

    private fun Project.createDownloadBuildTool(debugOption: SpigotDebug): Download {
        return tasks.create("downloadSpigotBuildTools", Download::class).apply {
            group += " spigot"
            description = "Download the BuildTools."
            source = BUILD_TOOLS_URL
            afterEvaluate {
                destination = debugOption.buildToolFile.file
            }
        }
    }

    private fun Project.createCleanSpigotBuild(debugOption: SpigotDebug): Delete {
        return tasks.create("cleanSpigotBuild", Delete::class).apply {
            group = "spigradle spigot"
            description = "Delete all BuildTools directories"
            delete(debugOption.buildToolFile.directory)
            delete(debugOption.buildToolOutputDirectory)
        }
    }

    private fun Project.createBuildSpigot(debugOption: SpigotDebug): JavaExec {
        return tasks.create("buildSpigot", JavaExec::class) {
            group = "spigradle spigot"
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
        return tasks.create("prepareSpigot") {
            group = "spigradle spigot"
            description = "Copy the spigot.jar generated by BuildTools into the given path."
            doLast {
                val (spigotJar, spigotDir) = debugOption.spigotFile
                val buildResultJar = runCatching {
                    debugOption.buildToolFile.directory.findBuildResultJar()
                }.getOrElse {
                    throw GradleException("Error while reading buildVersion in build info.json.", it)
                }
                copy {
                    into(spigotDir)
                    rename { spigotJar.name }
                    from(buildResultJar)
                }
            }
        }
    }

    private fun File.readBuildVersion() = SpigradlePlugin.json.readValue<Map<String, Any>>(
            File(this, "BuildData/info.json")
    )["minecraftVersion"]?.toString()

    private fun File.findBuildResultJar() = File(this, "spigot-${readBuildVersion()}.jar")

    private fun Project.createRunServer(name: String, workFile: FileEntry, debug: SpigotDebug): JavaExec {
        val (serverJar, serverDir) = workFile
        return tasks.create("run$name", JavaExec::class) {
            group = debugTaskGroup(name)
            description = "Startup the $name server."
            standardInput = System.`in`
            args("nogui", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=${debug.agentPort}")
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

    @OptIn(ExperimentalStdlibApi::class)
    private fun Project.createPreparePlugin(name: String, serverJar: FileEntry, spigot: SpigotExtension): Task {
        return tasks.create("preparePlugin$name") {
            group = debugTaskGroup(name)
            description = "Copy the jars into the $name server."
            doLast {
                val pluginsDir = File(serverJar.directory, "plugins")
                val pluginJar = tasks.withType<Jar>().asSequence().mapNotNull {
                    it.archiveFile.orNull?.asFile
                }.find {
                    it.isFile
                } ?: throw GradleException("Couldn't find a plugin.jar")
                val needPlugins = (spigot.depends + spigot.softDepends).toMutableSet()
                (pluginsDir.listFiles() ?: emptyArray()).asSequence().mapNotNull {
                    it.readPluginJar()["name"]?.toString()
                }.takeWhile {
                    needPlugins.isNotEmpty()
                }.forEach {
                    needPlugins -= it
                }
                copy {
                    project.withConvention(JavaPluginConvention::class) {
                        sourceSets["main"].compileClasspath
                    }.asSequence().mapNotNull { file ->
                        file.readPluginJar()["name"]?.let { pluginName ->
                            file to pluginName.toString()
                        }
                    }.takeWhile {
                        needPlugins.isNotEmpty()
                    }.filter { (_, pluginName) ->
                        needPlugins.remove(pluginName)
                    }.forEach { (file, _) ->
                        logger.info("Plugin copied from ${file.absolutePath}")
                        from(file)
                    }
                    from(pluginJar)
                    into(pluginsDir)
                }
            }
        }
    }

    private fun Project.createDebugRun(name: String): Task {
        return tasks.create("debug$name") {
            group = debugTaskGroup(name)
            description = "Startup the $name server with your plugin.jar"
        }
    }

    private fun Project.createDownloadPaper(debug: SpigotDebug): Task {
        return tasks.create("downloadPaper", Download::class).apply {
            group = DEBUG_TASK_PAPER_GROUP
            description = "Download the Paperclip."
            afterEvaluate {
                source = source ?: "https://papermc.io/api/v1/paper/${debug.buildVersion}/latest/download"
                destination = debug.paperFile.file
            }
        }
    }

    private fun Project.createPreparePaper(debug: SpigotDebug): Task {
        return tasks.create("preparePaper").apply {
            group = DEBUG_TASK_PAPER_GROUP
            description = "Download Paperclip for ready to run."
        }
    }
}