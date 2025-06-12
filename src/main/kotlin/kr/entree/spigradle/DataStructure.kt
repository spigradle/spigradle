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

package kr.entree.spigradle

import org.gradle.api.Project
import java.io.File

interface MainProvider {
    var main: String?
}

interface StandardDescription : MainProvider {
    var name: String?
    var version: String?
    var description: String?

    fun setDefault(project: Project) {
        name = name ?: project.name
        version = version ?: project.version.toString()
        description = description ?: project.description
    }
}

interface CommonDebug {
    var serverJar: File
    var serverDirectory: File
    var agentPort: Int

    /**
     * Program arguments for the server.
     */
    var args: List<Any>

    /**
     * JVM Arguments for the server.
     */
    var jvmArgs: List<Any>

    /**
     * Groovy DSL helper for the [args] configuration.
     */
    fun args(vararg args: String) {
        this.args = args.toList()
    }

    /**
     * Groovy DSL helper for the [jvmArgs] configuration.
     */
    fun jvmArgs(vararg jvmArgs: String) {
        this.jvmArgs = jvmArgs.toList()
    }
}