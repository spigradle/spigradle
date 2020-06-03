package kr.entree.spigradle.module.spigot

import com.fasterxml.jackson.module.kotlin.readValue
import kr.entree.spigradle.data.Load
import kr.entree.spigradle.data.SpigotRepositories
import kr.entree.spigradle.internal.Groovies
import kr.entree.spigradle.internal.findArtifactJar
import kr.entree.spigradle.internal.notNull
import kr.entree.spigradle.module.common.Download
import kr.entree.spigradle.module.common.SpigradlePlugin
import kr.entree.spigradle.module.common.applySpigradlePlugin
import kr.entree.spigradle.module.common.setupDescGenTask
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.Delete
import org.gradle.api.tasks.JavaExec
import org.gradle.kotlin.dsl.*
import org.gradle.kotlin.dsl.support.useToRun
import java.io.File
import java.util.jar.JarFile

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
        const val TASK_GROUP = "spigot"
        const val TASK_DEBUG_GROUP = "$TASK_GROUP debug"
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

    // prepareSpigot: downloadBuildTools -> buildSpigot -> copySpigot
    // preparePlugin: copyArtifactJar -> copyClasspathPlugins
    // debugSpigot: preparePlugin -> prepareSpigot -> runSpigot
    // debugPaper: preparePlugin -> downloadPaperclip -> runSpigot(Paper)
    private fun Project.setupSpigotDebugTasks() {
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
        val runSpigot = createRunSpigot(debugOption)
        val build by tasks
        val preparePlugin = createPreparePlugin(spigot).apply {
            dependsOn(build)
        }
        createDebugRun("Spigot").apply { // debugSpigot
            dependsOn(preparePlugin, prepareSpigot, runSpigot)
            runSpigot.mustRunAfter(preparePlugin, prepareSpigot)
        }
        createCleanSpigotBuild(debugOption)
        // Paper
        val paperClipDownload = createDownloadPaper(debugOption)
        createDebugRun("Paper").apply { // debugPaper
            dependsOn(preparePlugin, paperClipDownload, runSpigot)
            runSpigot.mustRunAfter(preparePlugin, paperClipDownload)
        }
    }

    private fun Project.createDownloadBuildTool(debugOption: SpigotDebug): Download {
        return tasks.create("downloadSpigotBuildTools", Download::class).apply {
            group = TASK_DEBUG_GROUP
            description = "Download the BuildTools."
            source = BUILD_TOOLS_URL
            afterEvaluate {
                destination = debugOption.buildToolJar
            }
        }
    }

    private fun Project.createCleanSpigotBuild(debugOption: SpigotDebug): Delete {
        return tasks.create("cleanSpigotBuild", Delete::class).apply {
            group = TASK_DEBUG_GROUP
            description = "Delete all BuildTools directories"
            delete(debugOption.buildToolDirectory)
            delete(debugOption.buildToolOutputDirectory)
        }
    }

    private fun Project.createBuildSpigot(options: SpigotDebug): JavaExec {
        return tasks.create("buildSpigot", JavaExec::class) {
            group = TASK_DEBUG_GROUP
            description = "Build the spigot.jar using the BuildTools."
            outputs.cacheIf { true }
            afterEvaluate {
                outputs.dir(File(options.buildToolJar.parentFile, "CraftBukkit/target/classes"))
                classpath = files(options.buildToolJar)
                workingDir = options.buildToolDirectory
                args(
                        "--rev", options.buildVersion,
                        "--output-dir", options.buildToolOutputDirectory
                )
            }
        }
    }

    private fun Project.createPrepareSpigot(options: SpigotDebug): Task {
        return tasks.create("prepareSpigot", Copy::class) {
            group = TASK_DEBUG_GROUP
            description = "Copy the spigot.jar generated by BuildTools into the given path."
            afterEvaluate {
                from(File(options.buildToolDirectory, "Spigot/Spigot-Server/target")) {
                    include("spigot*.jar")
                }
                into(options.serverDirectory)
                rename { options.serverJar.name }
            }
        }
    }

    private fun File.readBuildVersion() = SpigradlePlugin.json.readValue<Map<String, Any>>(
            File(this, "BuildData/info.json")
    )["minecraftVersion"]?.toString()

    private fun File.findBuildResultJar() = File(this, "spigot-${readBuildVersion()}.jar")

    private fun Project.createRunSpigot(debug: SpigotDebug): JavaExec {
        val serverJar = debug.serverJar
        return tasks.create("runSpigot", JavaExec::class) {
            group = TASK_DEBUG_GROUP
            description = "Startup the spigot server."
            standardInput = System.`in`
            args("nogui")
            afterEvaluate {
                classpath = files(serverJar)
                workingDir = serverJar.parentFile
                jvmArgs("-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=${debug.agentPort}")
            }
            doFirst {
                if (!debug.eula) {
                    throw GradleException("""
                        Please set the 'eula' property to true if you agree the Mojang EULA.
                        https://account.mojang.com/documents/minecraft_eula
                    """.trimIndent())
                }
                File(workingDir, "eula.txt").writeText("eula=true")
            }
        }
    }

    private fun Project.createPreparePlugin(spigot: SpigotExtension): Task {
        return tasks.create("preparePlugin") {
            group = TASK_DEBUG_GROUP
            description = "Copy the plugin jars into the server."
            doLast {
                val serverPluginsDir = File(spigot.debug.serverDirectory, "plugins")
                val pluginJar = notNull(tasks.findArtifactJar()) { "Couldn't find a plugin.jar" }
                val needPlugins = (spigot.depends + spigot.softDepends).toMutableSet()
                // Remove already had plugins
                (serverPluginsDir.listFiles() ?: emptyArray()).asSequence().takeWhile {
                    needPlugins.isNotEmpty()
                }.mapNotNull {
                    it.readBukkitPluginDescription()["name"]?.toString()
                }.forEach {
                    needPlugins -= it
                }
                copy {
                    from(pluginJar)
                    into(serverPluginsDir)
                    // Find depend plugins from classpath and copy into server
                    project.withConvention(JavaPluginConvention::class) {
                        sourceSets["main"].compileClasspath
                    }.asSequence().takeWhile {
                        needPlugins.isNotEmpty()
                    }.mapNotNull { file ->
                        file.readBukkitPluginDescription()["name"]?.let { pluginName ->
                            file to pluginName.toString()
                        }
                    }.filter { (_, pluginName) ->
                        needPlugins.remove(pluginName)
                    }.forEach { (file, _) ->
                        logger.info("Plugin copied from ${file.absolutePath}")
                        from(file)
                    }
                }
            }
        }
    }

    private fun Project.createDebugRun(name: String): Task {
        return tasks.create("debug$name") {
            group = TASK_DEBUG_GROUP
            description = "Startup the $name server with your plugin.jar"
        }
    }

    private fun Project.createDownloadPaper(debug: SpigotDebug): Task {
        return tasks.create("downloadPaper", Download::class).apply {
            group = TASK_DEBUG_GROUP
            description = "Download the Paperclip."
            afterEvaluate {
                source = source ?: "https://papermc.io/api/v1/paper/${debug.buildVersion}/latest/download"
                destination = debug.serverJar
            }
        }
    }
}

internal fun File.readBukkitPluginDescription() = runCatching {
    JarFile(this).run {
        getEntry("plugin.yml")?.run {
            getInputStream(this)
        }
    }?.useToRun {
        SpigradlePlugin.yaml.readValue<Map<String, Any>>(this)
    }
}.getOrNull() ?: emptyMap()