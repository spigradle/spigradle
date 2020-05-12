package kr.entree.spigradle.dependency

import kr.entree.spigradle.spigot.data.SpigotDependencies
import org.gradle.api.artifacts.dsl.DependencyHandler

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