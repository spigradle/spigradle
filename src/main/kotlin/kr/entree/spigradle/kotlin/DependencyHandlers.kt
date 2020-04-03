package kr.entree.spigradle.kotlin

import kr.entree.spigradle.SpigradleMeta
import kr.entree.spigradle.project.Dependencies.*
import org.gradle.api.artifacts.dsl.DependencyHandler

fun DependencyHandler.spigradle(version: String = SpigradleMeta.VERSION) = SPIGRADLE.format(version)

fun DependencyHandler.spigotmc(artifactId: String, version: String) = "${SPIGOT.groupId}:$artifactId:$version"

fun DependencyHandler.spigot(version: String = SPIGOT.defaultVersion) = SPIGOT.format(version)

fun DependencyHandler.spigotAll(version: String = SPIGOT_ALL.defaultVersion) = SPIGOT_ALL.format(version)

fun DependencyHandler.bungeecord(version: String = BUNGEECORD.defaultVersion) = BUNGEECORD.format(version)

fun DependencyHandler.minecraftServer(version: String = MINECRAFT_SERVER.defaultVersion) = MINECRAFT_SERVER.format(version)

fun DependencyHandler.paper(version: String = PAPER.defaultVersion) = PAPER.format(version)

fun DependencyHandler.bukkit(version: String = BUKKIT.defaultVersion) = BUKKIT.format(version)

fun DependencyHandler.craftbukkit(version: String = CRAFT_BUKKIT.defaultVersion) = CRAFT_BUKKIT.format(version)

fun DependencyHandler.protocolLib(version: String = PROTOCOL_LIB.defaultVersion) = PROTOCOL_LIB.format(version)

fun DependencyHandler.vault(version: String = VAULT.defaultVersion) = VAULT.format(version)

fun DependencyHandler.luckPerms(version: String = LUCK_PERMS.defaultVersion) = LUCK_PERMS.format(version)

fun DependencyHandler.worldedit(version: String = WORLD_EDIT.defaultVersion) = WORLD_EDIT.format(version)

fun DependencyHandler.worldguard(version: String = WORLD_GUARD.defaultVersion) = WORLD_GUARD.format(version)

fun DependencyHandler.essentialsX(version: String = ESSENTIALS_X.defaultVersion) = ESSENTIALS_X.format(version)

fun DependencyHandler.banManager(version: String = BAN_MANAGER.defaultVersion) = BAN_MANAGER.format(version)

fun DependencyHandler.commandhelper(version: String = COMMAND_HELPER.defaultVersion) = COMMAND_HELPER.format(version)

fun DependencyHandler.bStats(version: String = BSTATS.defaultVersion) = BSTATS.format(version)

fun DependencyHandler.bStatsLite(version: String = BSTATS_LITE.defaultVersion) = BSTATS_LITE.format(version)

fun DependencyHandler.lombok(version: String = LOMBOK.defaultVersion) = LOMBOK.format(version)