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

import kr.entree.spigradle.SpigradleMeta

/**
 * Created by JunHyung Lim on 2020-05-22
 */
object Dependencies {
    val LOMBOK = Dependency("org.projectlombok", "lombok", "1.18.12")
    val SPIGRADLE = Dependency("kr.entree", "spigradle", SpigradleMeta.VERSION)
}

object Repositories {
    val SONATYPE = "https://oss.sonatype.org/content/repositories/snapshots/"
    val JITPACK = "https://jitpack.io"
}

data class Dependency(
        val group: String,
        val name: String,
        val version: String,
        val versionModifier: (String) -> String = { it }
) {
    fun adjustVersion(inputVersion: String?) = inputVersion?.run(versionModifier) ?: version

    fun format(inputVersion: String? = null): String {
        return "$group:$name:${adjustVersion(inputVersion)}"
    }
}

inline fun Dependency(
        dependency: Dependency,
        group: String = dependency.group,
        name: String = dependency.name,
        version: String = dependency.version,
        noinline versionModifier: (String) -> String = dependency.versionModifier,
        configure: Dependency.() -> Unit = {}
) = Dependency(group, name, version, versionModifier).apply(configure)

object VersionModifier {
    val SNAPSHOT_APPENDER = createAdjuster("SNAPSHOT")
    val SPIGOT_ADJUSTER = createAdjuster("R0.1", "SNAPSHOT")

    fun createAdjuster(vararg tags: String): (String) -> String = { version ->
        buildString {
            val pieces = version.split("-")
            append(version)
            tags.drop(pieces.size - 1).forEach {
                append("-").append(it)
            }
        }
    }
}