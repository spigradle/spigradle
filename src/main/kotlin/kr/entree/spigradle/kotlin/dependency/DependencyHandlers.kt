package kr.entree.spigradle.kotlin.dependency

import kr.entree.spigradle.SpigradleMeta
import kr.entree.spigradle.project.Dependencies.*
import org.gradle.api.artifacts.dsl.DependencyHandler

fun DependencyHandler.spigradle(version: String = SpigradleMeta.VERSION) = SPIGRADLE.format(version)

fun DependencyHandler.spigotmc(artifactId: String, version: String) = "${SPIGOT.groupId}:$artifactId:$version"

fun DependencyHandler.spigot(version: String = "") = SPIGOT.format(version)

fun DependencyHandler.spigotAll(version: String = "") = SPIGOT_ALL.format(version)

fun DependencyHandler.minecraftServer(version: String = "") = MINECRAFT_SERVER.format(version)

fun DependencyHandler.paper(version: String = "") = PAPER.format(version)

fun DependencyHandler.bukkit(version: String = "") = BUKKIT.format(version)

fun DependencyHandler.craftbukkit(version: String = "") = CRAFT_BUKKIT.format(version)

fun DependencyHandler.protocolLib(version: String = "") = PROTOCOL_LIB.format(version)

fun DependencyHandler.vault(version: String = "") = VAULT.format(version)

fun DependencyHandler.luckPerms(version: String = "") = LUCK_PERMS.format(version)

fun DependencyHandler.worldedit(version: String = "") = WORLD_EDIT.format(version)

fun DependencyHandler.worldguard(version: String = "") = WORLD_GUARD.format(version)

fun DependencyHandler.commandhelper(version: String = "") = COMMAND_HELPER.format(version)

fun DependencyHandler.bStats(version: String = "") = BSTATS.format(version)

fun DependencyHandler.bStatsLite(version: String = "") = BSTATS_LITE.format(version)

fun DependencyHandler.lombok(version: String = "") = LOMBOK.format(version)