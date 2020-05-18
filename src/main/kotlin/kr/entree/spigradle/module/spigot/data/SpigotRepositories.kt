package kr.entree.spigradle.module.spigot.data

import kr.entree.spigradle.internal.SerialName

/**
 * Created by JunHyung Lim on 2020-05-07
 */
object SpigotRepositories {
    @SerialName("spigotmc")
    val SPIGOT_MC = "https://hub.spigotmc.org/nexus/content/repositories/snapshots/"
    val SONATYPE = "https://oss.sonatype.org/content/repositories/snapshots/"

    @SerialName("papermc")
    val PAPER_MC = "https://papermc.io/repo/repository/maven-public/"
    val PROTOCOL_LIB = "https://repo.dmulloy2.net/nexus/repository/public/"
    val JITPACK = "https://jitpack.io"

    @SerialName("enginehub")
    val ENGINE_HUB = "https://maven.enginehub.org/repo/"

    @SerialName("codemc")
    val CODE_MC = "https://repo.codemc.org/repository/maven-public/"

    @SerialName("enderZone")
    val ENDER_ZONE = "https://ci.ender.zone/plugin/repository/everything/"

    @SerialName("frostcast")
    val FROSTCAST = "https://ci.frostcast.net/plugin/repository/everything"
}

@OptIn(ExperimentalStdlibApi::class)
val SpigotRepositories.all by lazy {
    buildMap<String, String> {
        "spigot" to SpigotRepositories.SPIGOT_MC
    }
}