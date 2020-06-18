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

internal fun Project.createDebugConfigurations(name: String, debug: CommonDebug) {
    val idea: IdeaModel by extensions
    idea.project?.settings {
        runConfigurations {
            register("Debug$name", Remote::class) {
                host = "localhost"
                port = debug.agentPort
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
            args("nogui")
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
            descriptionFileName: String,
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
                    it.readYamlDescription(descriptionFileName)?.get(nameProperty)?.toString()
                }?.takeWhile {
                    !readyPlugins.containsAll(needPlugins)
                }?.forEach { readyPlugins += it }
                // Find depend plugins from classpath
                project.withConvention(JavaPluginConvention::class) {
                    sourceSets["main"].compileClasspath
                }.asSequence().mapNotNull { depFile ->
                    depFile.readYamlDescription(descriptionFileName)
                            ?.let { desc -> depFile to desc }
                }.takeWhile {
                    !readyPlugins.containsAll(needPlugins)
                }.filter { (_, desc) ->
                    val pluginName = desc["name"]?.toString()
                    pluginName != null && readyPlugins.add(pluginName)
                }.mapNotNull { it.first }.toList()
            })
        }
    }
}