package kr.entree.spigradle.project

import kr.entree.spigradle.SpigradleMeta

import static kr.entree.spigradle.project.Dependency.*

/**
 * Created by JunHyung Lim on 2020-02-29
 */
class Dependencies {
    // Bukkit
    public static final Dependency SPIGOT = dependency(
            'org.spigotmc',
            'spigot-api',
            '1.15.2-R0.1-SNAPSHOT',
            SPIGOT_VERSION_ADJUSTER
    )
    public static final Dependency SPIGOT_ALL = dependency(SPIGOT) {
        artifactId = 'spigot'
    }
    public static final Dependency BUNGEECORD = dependency(
            'net.md-5',
            'bungeecord-api',
            '1.15-SNAPSHOT',
            SNAPSHOT_APPENDER
    )
    public static final Dependency MINECRAFT_SERVER = dependency(SPIGOT) {
        artifactId = 'minecraft-server'
        defaultVersion = '1.15.2-SNAPSHOT'
        versionAdjuster = SNAPSHOT_APPENDER
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
            '4.5.0'
    )
    public static final Dependency VAULT = dependency(
            'com.github.MilkBowl',
            'VaultAPI',
            '1.7'
    )
    public static final Dependency LUCK_PERMS = dependency(
            'net.luckperms',
            'api',
            '5.1'
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
    public static final Dependency ESSENTIALS_X = dependency(
            'net.ess3',
            'EssentialsX',
            '2.17.2'
    )
    public static final Dependency BAN_MANAGER = dependency(
            'me.confuser.banmanager',
            'BanManagerBukkit',
            '7.1.0-SNAPSHOT'
    )
    public static final Dependency COMMAND_HELPER = dependency(
            'com.sk89q',
            'commandhelper',
            '3.3.4-SNAPSHOT'
    )
    // Libraries
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
    public static final Dependency SPIGRADLE = dependency(
            'kr.entree',
            'spigradle',
            SpigradleMeta.VERSION
    )

    public static final Dependency[] values = [
            SPIGOT, SPIGOT_ALL, BUNGEECORD, MINECRAFT_SERVER, PAPER, BUKKIT,
            CRAFT_BUKKIT, PROTOCOL_LIB, VAULT, LUCK_PERMS, WORLD_EDIT,
            WORLD_GUARD, COMMAND_HELPER, ESSENTIALS_X, BSTATS, BSTATS_LITE, LOMBOK
    ]
}
