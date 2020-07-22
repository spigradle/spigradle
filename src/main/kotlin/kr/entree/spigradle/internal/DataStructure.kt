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

package kr.entree.spigradle.internal

import org.gradle.api.Project
import java.io.File

/**
 * Created by JunHyung Lim on 2020-05-18
 */
interface MainProvider {
    var main: String?
}

interface StandardDescription : MainProvider {
    var name: String?
    var version: String?

    fun init(project: Project) {
        name = name ?: project.name
        version = version ?: project.version.toString()
    }
}

interface CommonDebug {
    var serverJar: File
    var serverDirectory: File
    var agentPort: Int
    var programArgs: List<Any>
    var jvmArgs: List<Any>
}