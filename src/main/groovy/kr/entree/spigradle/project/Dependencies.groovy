package kr.entree.spigradle.project

import static kr.entree.spigradle.project.Dependency.SPIGOT_VERSION_ADJUSTER
import static kr.entree.spigradle.project.Dependency.dependency

/**
 * Created by JunHyung Lim on 2020-02-29
 */
class Dependencies {
    public static final Dependency SPIGRADLE = dependency(
            'kr.entree',
            'spigradle',
            '1.2.0-SNAPSHOT' // TODO: Separate
    )
    // Bukkit
    public static final Dependency SPIGOT = dependency(
            'org.spigotmc',
            'spigot-api',
            '1.14.4-R0.1-SNAPSHOT',
            SPIGOT_VERSION_ADJUSTER
    )
    public static final Dependency SPIGOT_ALL = dependency(SPIGOT) {
        artifactId = 'spigot'
    }
    public static final Dependency MINECRAFT_SERVER = dependency(SPIGOT) {
        artifactId = 'minecraft-server'
        defaultVersion = '1.14.4-SNAPSHOT'
    }
    public static final Dependency PAPER = dependency(SPIGOT) {
        groupId = 'com.destroystokyo.paper'
        artifactId = 'paper-api'
    }
    public static final Dependency BUKKIT = dependency(SPIGOT) {
        groupId = 'org.bukkit'
        artifactId = 'bukkit'
    }
    public static final Dependency CRAFT_BUKKIT = dependency(BUKKIT) {
        artifactId = 'craftbukkit'
    }
    // Plugins
    public static final Dependency PROTOCOL_LIB = dependency(
            'com.comphenix.protocol',
            'ProtocolLib',
            '4.4.0'
    )
    public static final Dependency VAULT = dependency(
            'com.github.MilkBowl',
            'VaultAPI',
            '1.7'
    )
    public static final Dependency LUCK_PERMS = dependency(
            'me.lucko.luckperms',
            'luckperms-api',
            '5.0'
    )
    public static final Dependency WORLD_EDIT = dependency(
            'com.sk89q.worldedit',
            'worldedit-bukkit',
            '7.1.0'
    )
    public static final Dependency WORLD_GUARD = dependency(
            'com.sk89q.worldguard',
            'worldguard-bukkit',
            '7.0.2'
    )
    public static final Dependency COMMAND_HELPER = dependency(
            'com.sk89q',
            'commandhelper',
            '3.3.4-SNAPSHOT'
    )
    public static final Dependency BSTATS = dependency(
            'org.bstats',
            'bstats-bukkit',
            '1.7'
    )
    public static final Dependency BSTATS_LITE = dependency(BSTATS) {
        artifactId = 'bstats-bukkit-lite'
    }
    public static final Dependency LOMBOK = dependency(
            'org.projectlombok',
            'lombok',
            '1.18.12'
    )
    public static final Dependency[] values = [
            SPIGOT, SPIGOT_ALL, MINECRAFT_SERVER, PAPER, BUKKIT,
            CRAFT_BUKKIT, PROTOCOL_LIB, VAULT, LUCK_PERMS, WORLD_EDIT,
            WORLD_GUARD, COMMAND_HELPER, BSTATS, BSTATS_LITE, LOMBOK
    ]
}