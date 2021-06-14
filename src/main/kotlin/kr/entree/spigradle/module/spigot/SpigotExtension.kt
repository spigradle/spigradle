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

package kr.entree.spigradle.module.spigot

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import groovy.lang.Closure
import kr.entree.spigradle.data.Command
import kr.entree.spigradle.data.Load
import kr.entree.spigradle.data.Permission
import kr.entree.spigradle.data.SpigotDebug
import kr.entree.spigradle.internal.SerialName
import kr.entree.spigradle.internal.StandardDescription
import kr.entree.spigradle.internal.Transient
import kr.entree.spigradle.module.common.debugDir
import kr.entree.spigradle.module.common.spigotBuildToolDir
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.kotlin.dsl.container
import org.gradle.kotlin.dsl.newInstance
import org.gradle.util.ConfigureUtil
import java.io.File

/**
 * Spigot configuration for the 'plugin.yml' description, and debug settings.
 *
 * Groovy Example:
 * ```groovy
 * spigot {
 *   authors 'Me', 'Someone'
 *   depends 'ProtocolLib', 'Vault'
 *   apiVersion '1.15'
 *   load STARTUP
 *   commands {
 *     give {
 *       aliases 'giv', 'i'
 *       description 'Give command.'
 *       permission 'test.foo'
 *       permissionMessage 'You do not have the permission!'
 *       usage '/<command> [item] [amount]'
 *     }
 *   }
 *   permissions {
 *     'test.foo' {
 *       description 'Allows foo command'
 *       defaults 'true'
 *     }
 *     'test.*' {
 *       description 'Wildcard permission'
 *       defaults 'op'
 *       children = ['test.foo': true]
 *     }
 *   }
 *   debug {
 *     eula = true
 *   }
 * }
 * ```
 *
 * Kotlin Example:
 * ```kotlin
 * import kr.entree.spigradle.data.Load
 *
 * spigot {
 *   authors = "Me"
 *   depends = listOf("ProtocolLib", "Vault")
 *   apiVersion = "1.15"
 *   load = Load.STARTUP
 *   commands {
 *     create("give") {
 *       aliases = listOf("giv", "i")
 *       description = "Give command."
 *       permission = "test.foo"
 *       permissionMessage = "You do not have the permission!"
 *       usage = "/<command> [item] [amount]"
 *     }
 *   }
 *   permissions {
 *     create("test.foo") {
 *       description = "Allows foo command"
 *       defaults = "true"
 *     }
 *     create("test.*") {
 *       description = "Wildcard permission"
 *       defaults = "op"
 *       children = mapOf("test.foo" to true)
 *     }
 *   }
 * }
 * ```
 *
 * See: [https://www.spigotmc.org/wiki/plugin-yml/]
 */
@JsonPropertyOrder(
    "main", "name", "version", "description", "website",
    "authors", "api-version", "load", "prefix", "depend",
    "softdepend", "loadbefore", "libraries", "commands",
    "permissions"
)
open class SpigotExtension(project: Project) : StandardDescription {
    /**
     * The name of main class that extends [org.bukkit.plugin.java.JavaPlugin].
     *
     * Defaults to the class that auto-detected by [kr.entree.spigradle.module.common.SubclassDetection] or presented by [kr.entree.spigradle.annotations.Plugin].
     *
     * See: [https://www.spigotmc.org/wiki/plugin-yml/]
     */
    override var main: String? = null

    /**
     * The name of your plugin.
     *
     * Defaults to [org.gradle.api.Project.getName].
     *
     * See: [https://www.spigotmc.org/wiki/plugin-yml/]
     */
    override var name: String? = null

    /**
     * The version of your plugin.
     *
     * Defaults to [org.gradle.api.Project.getVersion]
     *
     * See: [https://www.spigotmc.org/wiki/plugin-yml/]
     */
    override var version: String? = null
    override var description: String? = null
    var website: String? = null
    var authors: List<String> = emptyList()

    @SerialName("api-version")
    var apiVersion: String? = null

    /**
     * The load order of your plugin.
     *
     * Groovy Example:
     * ```groovy
     * spigot {
     *   load = STARTUP // or POSTWORLD
     * }
     * ```
     *
     * Kotlin Example:
     *
     * ```kotlin
     * import kr.entree.spigradle.data.Load
     *
     * spigot {
     *   load = Load.STARTUP // or Load.POST_WORLD
     * }
     *
     * See: [https://www.spigotmc.org/wiki/plugin-yml/]
     * ```
     */
    var load: Load? = null
    var prefix: String? = null

    @SerialName("depend")
    var depends: List<String> = emptyList()

    @SerialName("softdepend")
    var softDepends: List<String> = emptyList()

    @SerialName("loadbefore")
    var loadBefore: List<String> = emptyList()

    /**
     * Runtime libraries of your plugin that will be loaded without shading(fat-jar) by Spigot 1.17 or higher.
     *
     * Example: `com.squareup.okhttp3:okhttp:4.9.0`
     *
     * See also: [Spigot & BungeeCord 1.17](https://www.spigotmc.org/threads/spigot-bungeecord-1-17.510208/#post-4184317)
     */
    // TODO: determine and filter?
    var libraries: List<String> = emptyList()

    /**
     * DSL container for the [commands] configuration.
     *
     * Groovy Example:
     * ```groovy
     * commands {
     *   give {
     *     aliases 'giv', 'i'
     *     description 'Give command.'
     *   }
     * }
     * ```
     *
     * Kotlin Example:
     *
     * ```kotlin
     * commands {
     *   create("give") {
     *     aliases = listOf("giv", "i")
     *     description = "Give command."
     *   }
     * }
     * ```
     *
     * See: [https://www.spigotmc.org/wiki/plugin-yml/]
     */
    val commands: NamedDomainObjectContainer<Command> = project.run { container { objects.newInstance(it) } }

    /**
     * DSL container for the [permissions] configuration.
     *
     * Groovy Example:
     * ```groovy
     * permissions {
     *   'test.foo' {
     *     description 'Allows foo command.'
     *     default 'true'
     *   }
     * }
     * ```
     *
     * Kotiln Example:
     * ```kotlin
     * permissions {
     *   create("test.foo") {
     *     description = "Allows foo command."
     *     default = "true"
     *   }
     * }
     * ```
     *
     * See: [https://www.spigotmc.org/wiki/plugin-yml/]
     */
    val permissions: NamedDomainObjectContainer<Permission> = project.run { container { objects.newInstance(it) } }

    /**
     * Configuration for the debug tasks like 'debugSpigot', 'buildSpigot'
     *
     * Example:
     * ```groovy
     * debug {
     *   eula true
     *   buildVersion '1.15.2'
     * }
     * ```
     */
    @Transient
    val debug: SpigotDebug = project.objects.newInstance(
        File(project.debugDir, "spigot/server.jar"),
        File(project.gradle.spigotBuildToolDir, "BuildTools.jar")
    )

    /**
     * Groovy DSL helper for [commands] configuration.
     */
    fun commands(closure: Closure<*>) {
        ConfigureUtil.configure(closure, commands)
    }

    /**
     * Kotlin DSL helper for [commands] configuration.
     */
    fun commands(configure: NamedDomainObjectContainer<Command>.() -> Unit) = configure(commands)

    /**
     * Groovy DSL helper for [permissions] configuration.
     */
    fun permissions(closure: Closure<*>) {
        ConfigureUtil.configure(closure, permissions)
    }

    /**
     * Kotlin DSL helper for [permissions] configuration.
     */
    fun permissions(configure: NamedDomainObjectContainer<Permission>.() -> Unit) = configure(permissions)

    /**
     * Groovy DSL helper for [debug] configuration.
     */
    fun debug(configure: Closure<*>) {
        ConfigureUtil.configure(configure, debug)
    }

    /**
     * Kotlin DSL helper for [debug] configuration.
     */
    fun debug(configure: SpigotDebug.() -> Unit) = configure(debug)

    /**
     * Groovy DSL helper for the [authors] configuration.
     */
    fun authors(vararg authors: String) {
        this.authors = authors.toList()
    }

    /**
     * Groovy DSL helper for the [depends] configuration.
     */
    fun depends(vararg depends: String) {
        this.depends = depends.toList()
    }

    /**
     * Groovy DSL helper for the [softDepends] configuration.
     */
    fun softDepends(vararg softDepends: String) {
        this.softDepends = softDepends.toList()
    }

    /**
     * Groovy DSL helper for the [loadBefore] configuration.
     */
    fun loadBefore(vararg loadBefore: String) {
        this.loadBefore = loadBefore.toList()
    }
}