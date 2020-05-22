package kr.entree.spigradle.kotlin

import kr.entree.spigradle.data.SpigotDependencies
import kr.entree.spigradle.data.SpigotRepositories
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.kotlin.dsl.maven

/**
 * Repositories
 */
fun RepositoryHandler.spigotmc(configure: MavenArtifactRepository.() -> Unit = {}) = maven(SpigotRepositories.SPIGOT_MC, configure)

fun RepositoryHandler.bungeecord(configure: MavenArtifactRepository.() -> Unit = {}) = sonatype(configure)

fun RepositoryHandler.papermc(configure: MavenArtifactRepository.() -> Unit = {}) = maven(SpigotRepositories.PAPER_MC, configure)

fun RepositoryHandler.protocolLib(configure: MavenArtifactRepository.() -> Unit = {}) = maven(SpigotRepositories.PROTOCOL_LIB, configure)

fun RepositoryHandler.vault(configure: MavenArtifactRepository.() -> Unit = {}) = jitpack(configure)

fun RepositoryHandler.enginehub(configure: MavenArtifactRepository.() -> Unit = {}) = maven(SpigotRepositories.ENGINE_HUB, configure)

fun RepositoryHandler.codemc(configure: MavenArtifactRepository.() -> Unit = {}) = maven(SpigotRepositories.CODE_MC, configure)

fun RepositoryHandler.enderZone(configure: MavenArtifactRepository.() -> Unit = {}) = maven(SpigotRepositories.ENDER_ZONE, configure)

fun RepositoryHandler.essentialsX(configure: MavenArtifactRepository.() -> Unit = {}) = enderZone(configure)

fun RepositoryHandler.frostcast(configure: MavenArtifactRepository.() -> Unit = {}) = maven(SpigotRepositories.FROSTCAST, configure)

fun RepositoryHandler.banManager(configure: MavenArtifactRepository.() -> Unit = {}) = frostcast(configure)

fun RepositoryHandler.bStats(configure: MavenArtifactRepository.() -> Unit = {}) = codemc(configure)

/**
 * Dependencies
 */
fun DependencyHandler.spigotmc(artifactId: String, version: String? = null) =
        "${SpigotDependencies.SPIGOT.group}:$artifactId:${version ?: SpigotDependencies.SPIGOT.version}"

fun DependencyHandler.spigot(version: String? = null) = SpigotDependencies.SPIGOT.format(version)

fun DependencyHandler.spigotAll(version: String? = null) = SpigotDependencies.SPIGOT_ALL.format(version)

fun DependencyHandler.bungeecord(version: String? = null) = SpigotDependencies.BUNGEE_CORD.format(version)

fun DependencyHandler.minecraftServer(version: String? = null) = SpigotDependencies.MINECRAFT_SERVER.format(version)

fun DependencyHandler.paper(version: String? = null) = SpigotDependencies.PAPER.format(version)

fun DependencyHandler.bukkit(version: String? = null) = SpigotDependencies.BUKKIT.format(version)

fun DependencyHandler.craftbukkit(version: String? = null) = SpigotDependencies.CRAFT_BUKKIT.format(version)

fun DependencyHandler.protocolLib(version: String? = null) = SpigotDependencies.PROTOCOL_LIB.format(version)

fun DependencyHandler.vault(version: String? = null) = SpigotDependencies.VAULT.format(version)

fun DependencyHandler.luckPerms(version: String? = null) = SpigotDependencies.LUCK_PERMS.format(version)

fun DependencyHandler.worldedit(version: String? = null) = SpigotDependencies.WORLD_EDIT.format(version)

fun DependencyHandler.worldguard(version: String? = null) = SpigotDependencies.WORLD_GUARD.format(version)

fun DependencyHandler.essentialsX(version: String? = null) = SpigotDependencies.ESSENTIALS_X.format(version)

fun DependencyHandler.banManager(version: String? = null) = SpigotDependencies.BAN_MANAGER.format(version)

fun DependencyHandler.commandhelper(version: String? = null) = SpigotDependencies.COMMAND_HELPER.format(version)

fun DependencyHandler.bStats(version: String? = null) = SpigotDependencies.B_STATS.format(version)

fun DependencyHandler.bStatsLite(version: String? = null) = SpigotDependencies.B_STATS_LITE.format(version)