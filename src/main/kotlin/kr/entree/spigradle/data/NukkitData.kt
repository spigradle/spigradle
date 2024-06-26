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

package kr.entree.spigradle.data

import kr.entree.spigradle.internal.CommonDebug
import java.io.File
import javax.inject.Inject

object NukkitRepositories {
    val NUKKIT_X = "https://repo.nukkitx.com/maven-snapshots"
}

object NukkitDependencies {
    val NUKKIT = Dependency(
            "cn.nukkit",
            "nukkit",
            "2.0.0-SNAPSHOT",
            false,
            VersionModifier.SNAPSHOT_APPENDER
    )
    val NUKKIT_X = NUKKIT
}

open class NukkitDebug(
        override var serverJar: File,
        override var serverDirectory: File,
        override var agentPort: Int
) : CommonDebug {
    override var args: List<Any> = listOf("nogui")
    override var jvmArgs: List<Any> = emptyList()

    @Inject
    constructor(serverJar: File) : this(serverJar, serverJar.parentFile, 5005)
}