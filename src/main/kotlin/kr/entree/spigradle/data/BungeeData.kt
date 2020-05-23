package kr.entree.spigradle.data

import kr.entree.spigradle.internal.SerialName

/**
 * Created by JunHyung Lim on 2020-05-23
 */
object BungeeRepositories {
    val BUNGEECORD = Repositories.SONATYPE
}

object BungeeDependencies {
    @SerialName("bungeecord")
    val BUNGEE_CORD = Dependency(
            "net.md-5",
            "bungeecord-api",
            "1.15-SNAPSHOT",
            VersionModifier.SNAPSHOT_APPENDER
    )
}