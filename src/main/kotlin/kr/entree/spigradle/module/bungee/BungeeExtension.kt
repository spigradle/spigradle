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

package kr.entree.spigradle.module.bungee

import groovy.lang.Closure
import kr.entree.spigradle.data.BungeeDebug
import kr.entree.spigradle.internal.StandardDescription
import kr.entree.spigradle.module.common.debugDir
import org.gradle.api.Project
import org.gradle.kotlin.dsl.newInstance
import org.gradle.util.ConfigureUtil
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
    override var description: String? = null
    var author: String? = null
    var depends: List<String> = emptyList()
    var softDepends: List<String> = emptyList()

    @Transient
    val debug: BungeeDebug = project.objects.newInstance(File(project.debugDir, "bungee/bungee.jar"))

    fun debug(configure: Closure<*>) {
        ConfigureUtil.configure(configure, debug)
    }

    fun debug(configure: BungeeDebug.() -> Unit) = configure(debug)

    fun depends(vararg depends: String) {
        this.depends = depends.toList()
    }

    fun softDepends(vararg softDepends: String) {
        this.softDepends = softDepends.toList()
    }
}