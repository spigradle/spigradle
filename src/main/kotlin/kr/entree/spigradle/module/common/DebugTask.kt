package kr.entree.spigradle.module.common

import kr.entree.spigradle.internal.cachingProvider
import kr.entree.spigradle.internal.findArtifactJar
import kr.entree.spigradle.internal.lazyString
import org.gradle.api.Project
import org.gradle.api.logging.LogLevel
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withConvention
import java.io.File

/**
 * Created by JunHyung Lim on 2020-06-07
 */
object DebugTask {
    fun Project.registerRunServer(name: String, agentPort: Int = 5005): TaskProvider<JavaExec> {
        return tasks.register(name, JavaExec::class) {
            standardInput = System.`in`
            logging.captureStandardOutput(LogLevel.LIFECYCLE)
            jvmArgs(lazyString { "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=${agentPort}" })
            args("nogui")
        }
    }

    fun Project.registerPreparePlugin(
            name: String,
            nameProperty: String,
            dependPlugins: () -> Iterable<String>,
            descriptionReader: (File) -> Map<String, Any>?
    ): TaskProvider<Copy> {
        return tasks.register(name, Copy::class) {
            description = "Copy the plugin jars"
            from(cachingProvider { tasks.findArtifactJar() ?: files() })
            from(cachingProvider {
                val needPlugins = dependPlugins().toMutableSet()
                val readyPlugins = mutableSetOf<String>()
                // Remove already had plugins
                destinationDir.listFiles { _, name ->
                    name.endsWith(".jar")
                }?.asSequence()?.mapNotNull {
                    descriptionReader(it)?.get(nameProperty)?.toString()
                }?.takeWhile {
                    readyPlugins.containsAll(needPlugins)
                }?.forEach { readyPlugins += it }
                // Find depend plugins from classpath
                project.withConvention(JavaPluginConvention::class) {
                    sourceSets["main"].compileClasspath
                }.asSequence().mapNotNull { depFile ->
                    descriptionReader(depFile)?.let { desc -> depFile to desc }
                }.takeWhile {
                    readyPlugins.containsAll(needPlugins)
                }.filter { (_, desc) ->
                    val pluginName = desc["name"]?.toString()
                    pluginName != null && readyPlugins.add(pluginName)
                }.mapNotNull { it.first }.toList()
            })
        }
    }
}