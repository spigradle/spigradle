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

package kr.entree.spigradle.kotlin

import kr.entree.spigradle.data.Dependencies
import kr.entree.spigradle.data.Repositories
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.kotlin.dsl.maven

/**
 * The repo shortcut for Sonatype, related with Bungeecord.
 */
fun RepositoryHandler.sonatype(configure: MavenArtifactRepository.() -> Unit = {}) = maven(Repositories.SONATYPE, configure)

/**
 * The repo shortcut for Jitpack, related with Vault.
 */
fun RepositoryHandler.jitpack(configure: MavenArtifactRepository.() -> Unit = {}) = maven(Repositories.JITPACK, configure)

/**
 * The dependency shortcut for Spigradle.
 */
fun DependencyHandler.spigradle(version: String? = null) = Dependencies.SPIGRADLE.format(version)

/**
 * The dependency shortcut for Lombok.
 */
fun DependencyHandler.lombok(version: String? = null) = Dependencies.LOMBOK.format(version)