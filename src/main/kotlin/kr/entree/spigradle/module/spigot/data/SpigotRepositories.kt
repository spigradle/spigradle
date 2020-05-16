package kr.entree.spigradle.module.spigot.data

/**
 * Created by JunHyung Lim on 2020-05-07
 */
object SpigotRepositories {
    const val SPIGOT_MC = "https://hub.spigotmc.org/nexus/content/repositories/snapshots/"
    const val SONATYPE = "https://oss.sonatype.org/content/repositories/snapshots/"
    const val PAPER_MC = "https://papermc.io/repo/repository/maven-public/"
    const val PROTOCOL_LIB = "https://repo.dmulloy2.net/nexus/repository/public/"
    const val JITPACK = "https://jitpack.io"
    const val ENGINE_HUB = "https://maven.enginehub.org/repo/"
    const val CODE_MC = "https://repo.codemc.org/repository/maven-public/"
    const val ENDER_ZONE = "https://ci.ender.zone/plugin/repository/everything/"
    const val FROSTCAST = "https://ci.frostcast.net/plugin/repository/everything"
}

@OptIn(ExperimentalStdlibApi::class)
val SpigotRepositories.all by lazy {
    buildMap<String, String> {
        "spigot" to SpigotRepositories.SPIGOT_MC
    }
}