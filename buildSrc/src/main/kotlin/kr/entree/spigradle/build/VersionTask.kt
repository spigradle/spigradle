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

package kr.entree.spigradle.build

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option
import org.gradle.kotlin.dsl.property
import java.io.File

/**
 * Created by JunHyung Lim on 2020-06-30
 */
@Suppress("UnstableApiUsage")
open class VersionTask : DefaultTask() {
    companion object {
        val versionFile get() = File("version.txt")
        fun readVersion() = versionFile.readText()
    }

    @Input
    @Option(option = "build-version", description = "Configure the version of Spigradle.")
    val version = project.objects.property<String>()

    @TaskAction
    fun execute() {
        versionFile.writeText(version.get())
    }
}