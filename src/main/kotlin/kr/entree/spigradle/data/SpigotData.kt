package kr.entree.spigradle.data

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import kr.entree.spigradle.internal.CommonDebug
import kr.entree.spigradle.internal.SerialName
import kr.entree.spigradle.internal.Transient
import java.io.File

/**
 * Created by JunHyung Lim on 2020-05-22
 */
data class SpigotDebug(
        var serverJar: File,
        var buildToolJar: File,
        var serverDirectory: File = serverJar.parentFile,
        var buildToolDirectory: File = buildToolJar.parentFile,
        var buildToolOutputDirectory: File = File(buildToolDirectory, "outputs"),
        override var agentPort: Int = 5005,
        var eula: Boolean = false,
        var buildVersion: String = "1.15.2"
) : CommonDebug

enum class Load {
    @SerialName("POSTWORLD")
    POST_WORLD,
    STARTUP
}

@JsonPropertyOrder("description", "usage", "permission", "permission-message")
open class Command(@Transient val name: String) {
    var description: String? = null
    var usage: String? = null
    var permission: String? = null

    @SerialName("permission-message")
    var permissionMessage: String? = null
    var aliases = emptyList<String>()
}

@JsonPropertyOrder("description", "default", "children")
open class Permission(@Transient val name: String) {
    var description: String? = null

    @SerialName("default")
    var defaults: String? = null
    var children = emptyMap<String, Boolean>()
}

object SpigotRepositories {
    @SerialName("spigotmc")
    val SPIGOT_MC = "https://hub.spigotmc.org/nexus/content/repositories/snapshots/"
    val SPIGOT = SPIGOT_MC

    @SerialName("papermc")
    val PAPER_MC = "https://papermc.io/repo/repository/maven-public/"
    val PAPER = PAPER_MC
    val PROTOCOL_LIB = "https://repo.dmulloy2.net/nexus/repository/public/"
    val VAULT = Repositories.JITPACK

    @SerialName("enginehub")
    val ENGINE_HUB = "https://maven.enginehub.org/repo/"

    @SerialName("codemc")
    val CODE_MC = "https://repo.codemc.org/repository/maven-public/"
    val B_STATS = CODE_MC
    val ENDER_ZONE = "https://ci.ender.zone/plugin/repository/everything/"
    val ESSENTIALS_X = ENDER_ZONE
    val FROSTCAST = "https://ci.frostcast.net/plugin/repository/everything"
    val BAN_MANAGER = FROSTCAST
}

object SpigotDependencies {
    val SPIGOT = Dependency(
            "org.spigotmc",
            "spigot-api",
            "1.15.2-R0.1-SNAPSHOT",
            VersionModifier.SPIGOT_ADJUSTER
    )
    val SPIGOT_ALL = Dependency(SPIGOT, name = "spigot")
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