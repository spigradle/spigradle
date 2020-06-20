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

import kr.entree.spigradle.data.SpigotDependencies
import kr.entree.spigradle.data.SpigotRepositories
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.kotlin.dsl.maven

// Repositories
/**
 * The repo shortcut for SpigotMC, related with Spigot, MockBukkit...
 */
fun RepositoryHandler.spigotmc(configure: MavenArtifactRepository.() -> Unit = {}) = maven(SpigotRepositories.SPIGOT_MC, configure)

/**
 * The repo shortcut for PaperMC, related with Paper.
 */
fun RepositoryHandler.papermc(configure: MavenArtifactRepository.() -> Unit = {}) = maven(SpigotRepositories.PAPER_MC, configure)

/**
 * The repo shortcut for ProtocolLib(dumolly2), related with ProtocolLib.
 */
fun RepositoryHandler.protocolLib(configure: MavenArtifactRepository.() -> Unit = {}) = maven(SpigotRepositories.PROTOCOL_LIB, configure)

/**
 * The repo shortcut for Jitpack same as jitpack(), related with Vault.
 */
fun RepositoryHandler.vault(configure: MavenArtifactRepository.() -> Unit = {}) = jitpack(configure)

/**
 * The repo shortcut for EngineHub, related with worldedit, worldguard, commandhelper...
 */
fun RepositoryHandler.enginehub(configure: MavenArtifactRepository.() -> Unit = {}) = maven(SpigotRepositories.ENGINE_HUB, configure)

/**
 * The repo shortcut for CodeMC, related with bStats, bStatsLite.
 */
fun RepositoryHandler.codemc(configure: MavenArtifactRepository.() -> Unit = {}) = maven(SpigotRepositories.CODE_MC, configure)

/**
 * The repo shortcut for BStats same as codemc(), related with bStats and bStatsLite,
 */
fun RepositoryHandler.bStats(configure: MavenArtifactRepository.() -> Unit = {}) = codemc(configure)

/**
 * The repo shortcut for Ender.Zone, related with EssentialsX.
 */
fun RepositoryHandler.enderZone(configure: MavenArtifactRepository.() -> Unit = {}) = maven(SpigotRepositories.ENDER_ZONE, configure)

/**
 * The repo shortcut for Ender.Zone same as enderZone(), related with EssentialsX.
 */
fun RepositoryHandler.essentialsX(configure: MavenArtifactRepository.() -> Unit = {}) = enderZone(configure)


/**
 * The repo shortcut for frostcast, related with BanManager.
 */
fun RepositoryHandler.frostcast(configure: MavenArtifactRepository.() -> Unit = {}) = maven(SpigotRepositories.FROSTCAST, configure)

/**
 * The repo shortcut for frostcast same as frostcast(), related with BanManager.
 */
fun RepositoryHandler.banManager(configure: MavenArtifactRepository.() -> Unit = {}) = frostcast(configure)

// Dependencies
/**
 * The notation shortcut for spigotmc, format to "org.spigotmc:$artifactId:$version"
 */
fun DependencyHandler.spigotmc(artifactId: String, version: String? = null) =
        "${SpigotDependencies.SPIGOT.group}:$artifactId:${version ?: SpigotDependencies.SPIGOT.version}"

/**
 * The dependency shortcut for Spigot-API
 */
fun DependencyHandler.spigot(version: String? = null) = SpigotDependencies.SPIGOT.format(version)

/**
 * The dependency shortcut for Spigot, requires repository mavenLocal() published by BuildTools.
 */
fun DependencyHandler.spigotAll(version: String? = null) = SpigotDependencies.SPIGOT_ALL.format(version)

/**
 * The dependency shortcut for the NMS, requires repository mavenLocal() published by BuildTools.
 */
fun DependencyHandler.minecraftServer(version: String? = null) = SpigotDependencies.MINECRAFT_SERVER.format(version)

/**
 * The dependency shortcut for Paper, requires repository papermc()
 */
fun DependencyHandler.paper(version: String? = null) = SpigotDependencies.PAPER.format(version)

/**
 * The dependency shortcut for Bukkit, requires repository mavenLocal() published by BuildTools.
 */
fun DependencyHandler.bukkit(version: String? = null) = SpigotDependencies.BUKKIT.format(version)

/**
 * The dependency shortcut for CraftBukkit, requires repository mavenLocal() published by BuildTools.
 */
fun DependencyHandler.craftbukkit(version: String? = null) = SpigotDependencies.CRAFT_BUKKIT.format(version)

/**
 * The dependency shortcut for ProtocolLib, requires repository protocolLib()
 */
fun DependencyHandler.protocolLib(version: String? = null) = SpigotDependencies.PROTOCOL_LIB.format(version)

/**
 * The dependency shortcut for Vault, requires repository jitpack() or vault()
 */
fun DependencyHandler.vault(version: String? = null) = SpigotDependencies.VAULT.format(version)

/**
 * The dependency shortcut for LuckPerms, requires repository mavenCentral().
 */
fun DependencyHandler.luckPerms(version: String? = null) = SpigotDependencies.LUCK_PERMS.format(version)

/**
 * The dependency shortcut for WorldEdit, requires repository enginehub().
 */
fun DependencyHandler.worldedit(version: String? = null) = SpigotDependencies.WORLD_EDIT.format(version)

/**
 * The dependency shortcut for WorldGuard, requires repository enginehub().
 */
fun DependencyHandler.worldguard(version: String? = null) = SpigotDependencies.WORLD_GUARD.format(version)

/**
 * The dependency shortcut for EssentialsX, requires repository enderZone() or essentialsX().
 */
fun DependencyHandler.essentialsX(version: String? = null) = SpigotDependencies.ESSENTIALS_X.format(version)

/**
 * The dependency shortcut for BanManager, requires repository frostcast() or banManager().
 */
fun DependencyHandler.banManager(version: String? = null) = SpigotDependencies.BAN_MANAGER.format(version)

/**
 * The dependency shortcut for CommandHelper, requires repository enginehub().
 */
fun DependencyHandler.commandhelper(version: String? = null) = SpigotDependencies.COMMAND_HELPER.format(version)

/**
 * The dependency shortcut for BStats, requires repository codemc() or bStats().
 */
fun DependencyHandler.bStats(version: String? = null) = SpigotDependencies.B_STATS.format(version)

/**
 * The dependency shortcut for BStats-Lite, requires repository codemc() or bStats().
 */
fun DependencyHandler.bStatsLite(version: String? = null) = SpigotDependencies.B_STATS_LITE.format(version)

/**
 * The notation shortcut for MockBukkit, requires repository spigotmc() or spigot().
 *
 * It will format to "com.github.seeseemelk:MockBukkit-v[spigotVersion]:[mockBukkitVersion]-SNAPSHOT"
 *
 * @param spigotVersion The MockBukkit's spigot version, 'major.minor'. Defaults to "1.15"
 * @param mockBukkitVersion The MockBukkit's version. Defaults to "0.3.0"
 */
fun DependencyHandler.mockBukkit(spigotVersion: String? = null, mockBukkitVersion: String? = null) =
        "com.github.seeseemelk:MockBukkit-v${spigotVersion ?: "1.15"}:${mockBukkitVersion ?: "0.3.0"}-SNAPSHOT"