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
import kr.entree.spigradle.internal.SerialName
import java.io.File
import javax.inject.Inject

/**
 * Created by JunHyung Lim on 2020-05-23
 */
object BungeeRepositories {
    val BUNGEECORD = Repositories.SONATYPE
}

object BungeeDependencies {
    @SerialName("bungeecord")
    val BUNGEE_CORD = Dependency(
            "net.md-5",
            "bungeecord-api",
            "1.15-SNAPSHOT",
            VersionModifier.SNAPSHOT_APPENDER
    )
}

open class BungeeDebug(
        override var serverJar: File,
        override var serverDirectory: File,
        override var agentPort: Int
) : CommonDebug {
    @Inject
    constructor(serverJar: File) : this(serverJar, serverJar.parentFile, 5005)
}