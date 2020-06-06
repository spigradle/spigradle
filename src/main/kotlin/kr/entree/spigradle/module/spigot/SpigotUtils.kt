package kr.entree.spigradle.module.spigot

import com.fasterxml.jackson.module.kotlin.readValue
import kr.entree.spigradle.internal.Jackson
import org.gradle.api.tasks.SourceSet
import org.gradle.kotlin.dsl.support.useToRun
import java.io.File
import java.util.jar.JarFile

/**
 * Created by JunHyung Lim on 2020-06-06
 */
fun File.readBukkitPluginDescription() =
        runCatching {
            JarFile(this).run {
                getEntry("plugin.yml")?.run {
                    getInputStream(this)
                }
            }?.useToRun {
                Jackson.YAML.readValue<Map<String, Any>>(this)
            }
        }.getOrNull()

fun File.readAllBukkitPluginDescription() =
        listFiles { _, name -> name.endsWith(".jar") }
                ?.asSequence()?.mapNotNull {
                    it.readBukkitPluginDescription()
                } ?: emptySequence()

fun SourceSet.findBukkitPluginFromClasspath() =
        compileClasspath.asSequence().mapNotNull { depFile ->
            val pluginDesc = depFile.readBukkitPluginDescription()
            if (pluginDesc != null) depFile to pluginDesc else null
        }