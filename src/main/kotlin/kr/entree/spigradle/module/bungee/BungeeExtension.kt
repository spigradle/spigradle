package kr.entree.spigradle.module.bungee

import groovy.lang.Closure
import kr.entree.spigradle.data.BungeeDebug
import kr.entree.spigradle.internal.StandardDescription
import kr.entree.spigradle.module.common.debugDir
import org.gradle.api.Project
import org.gradle.kotlin.dsl.newInstance
import java.io.File

/**
 * Bungeecord configuration for the 'plugin.yml' description, and debug settings.
 *
 * Groovy Example:
 * ```groovy
 * spigot {
 *   author 'Me'
 *   depends 'ProtocolLib', 'Vault'
 *   debug {
 *     agentPort 5005
 *   }
 * }
 * ```
 *
 * Kotlin Example:
 * ```kotlin
 * import kr.entree.spigradle.data.Load
 *
 * spigot {
 *   author = "Me"
 *   depends = listOf("ProtocolLib", "Vault")
 *   debug {
 *     agentPort = 5005
 *   }
 * }
 * ```
 *
 * See: [https://www.spigotmc.org/wiki/create-your-first-bungeecord-plugin-proxy-spigotmc/#making-it-load]
 */
open class BungeeExtension(project: Project) : StandardDescription {
    override var main: String? = null
    override var name: String? = null
    override var version: String? = null
    var description: String? = null
    var author: String? = null
    var depends: List<String> = emptyList()
    var softDepends: List<String> = emptyList()

    @Transient
    val debug: BungeeDebug = project.objects.newInstance(File(project.debugDir, "bungee/bungee.jar"))

    fun debug(configure: Closure<*>) {
        configure.delegate = debug
        configure.call()
    }

    fun debug(configure: BungeeDebug.() -> Unit) = configure(debug)

    fun depends(vararg depends: String) {
        this.depends = depends.toList()
    }

    fun softDepends(vararg softDepends: String) {
        this.softDepends = softDepends.toList()
    }
}