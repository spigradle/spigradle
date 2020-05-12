package kr.entree.spigradle.spigot.data

import kotlinx.serialization.SerialName
import kr.entree.spigradle.data.Dependency
import kr.entree.spigradle.data.VersionModifier

/**
 * Created by JunHyung Lim on 2020-05-07
 */
object SpigotDependencies {
    val SPIGOT = Dependency(
            "org.spigotmc",
            "spigot-api",
            "1.15.2-R0.1-SNAPSHOT",
            VersionModifier.SPIGOT_ADJUSTER
    )
    val SPIGOT_ALL = Dependency(SPIGOT, name = "spigot")

    @SerialName("bungeecord")
    val BUNGEE_CORD = Dependency(
            "net.md-5",
            "bungeecord-api",
            "1.15-SNAPSHOT",
            VersionModifier.SNAPSHOT_APPENDER
    )
    val MINECRAFT_SERVER = Dependency(
            SPIGOT.group,
            "minecraft-server",
            "1.15.2-SNAPSHOT",
            VersionModifier.SNAPSHOT_APPENDER
    )
    val PAPER = Dependency(SPIGOT, "com.destroystokyo.paper", "paper-api")
    val BUKKIT = Dependency(SPIGOT, group = "org.bukkit", name = "bukkit")

    @SerialName("craftbukkit")
    val CRAFT_BUKKIT = Dependency(BUKKIT, name = "craftbukkit")
    val PROTOCOL_LIB = Dependency(
            "com.comphenix.protocol",
            "ProtocolLib",
            "4.5.0"
    )
    val VAULT = Dependency(
            "com.github.MilkBowl",
            "VaultAPI",
            "1.7"
    )
    val LUCK_PERMS = Dependency(
            "net.luckperms",
            "api",
            "5.1"
    )

    @SerialName("worldedit")
    val WORLD_EDIT = Dependency(
            "com.sk89q.worldedit",
            "worldedit-bukkit",
            "7.1.0"
    )

    @SerialName("worldguard")
    val WORLD_GUARD = Dependency(
            "com.sk89q.worldguard",
            "worldguard-bukkit",
            "7.0.2"
    )
    val ESSENTIALS_X = Dependency(
            "net.ess3",
            "EssentialsX",
            "2.17.2"
    )
    val BAN_MANAGER = Dependency(
            "me.confuser.banmanager",
            "BanManagerBukkit",
            "7.1.0-SNAPSHOT"
    )

    @SerialName("commandhelper")
    val COMMAND_HELPER = Dependency(
            "com.sk89q",
            "commandhelper",
            "3.3.4-SNAPSHOT"
    )
    val B_STATS = Dependency(
            "org.bstats",
            "bstats-bukkit",
            "1.7"
    )
    val B_STATS_LITE = Dependency(B_STATS, name = "bstats-bukkit-lite")
}