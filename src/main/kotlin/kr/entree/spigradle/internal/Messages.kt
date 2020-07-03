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

/**
 * Created by JunHyung Lim on 2020-05-18
 */
internal object Messages {
    fun noMainFound(extensionName: String, taskName: String) = """
        Spigradle couldn't find main class automatically!
        Please present your main class using the annotation @kr.entree.spigradle.annotations.PluginMain or @Plugin,
        or set the 'main' property in $extensionName {} block on build.gradle,
        or just disable $taskName task: 'tasks.$taskName.enabled = false'
    """.trimIndent()
}