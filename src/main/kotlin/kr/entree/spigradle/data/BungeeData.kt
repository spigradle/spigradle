package kr.entree.spigradle.data

import kr.entree.spigradle.internal.CommonDebug
import kr.entree.spigradle.internal.SerialName
import java.io.File
import javax.inject.Inject

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

open class BungeeDebug(
        override var serverJar: File,
        override var serverDirectory: File,
        override var agentPort: Int
) : CommonDebug {
    @Inject
    constructor(serverJar: File) : this(serverJar, serverJar.parentFile, 5005)
}