package kr.entree.spigradle.module.bungee

import com.fasterxml.jackson.module.kotlin.readValue
import kr.entree.spigradle.internal.Jackson
import org.gradle.api.tasks.SourceSet
import org.gradle.kotlin.dsl.support.useToRun
import java.io.File
import java.util.jar.JarFile

/**
 * Created by JunHyung Lim on 2020-06-07
 */
// No idea to predicate between bungee.yml and plugin.yml
internal fun File.readBungeePluginDescription() =
        runCatching {
            JarFile(this).run {
                getEntry("bungee.yml")?.run {
                    getInputStream(this)
                }
            }?.useToRun {
                Jackson.YAML.readValue<Map<String, Any>>(this)
            }
        }.getOrNull()

internal fun File.readAllBungeePluginDescription() =
        listFiles { _, name -> name.endsWith(".jar") }
                ?.asSequence()?.mapNotNull {
                    it.readBungeePluginDescription()
                } ?: emptySequence()

internal fun SourceSet.findBungeePluginFromClasspath() =
        compileClasspath.asSequence().mapNotNull { depFile ->
            depFile.readBungeePluginDescription()?.run { depFile to this }
        }