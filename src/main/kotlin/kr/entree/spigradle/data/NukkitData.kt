package kr.entree.spigradle.data

import kr.entree.spigradle.internal.CommonDebug
import java.io.File
import javax.inject.Inject

/**
 * Created by JunHyung Lim on 2020-05-23
 */
object NukkitRepositories {
    val NUKKIT_X = "https://repo.nukkitx.com/maven-snapshots"
}

object NukkitDependencies {
    val NUKKIT = Dependency(
            "cn.nukkit",
            "nukkit",
            "1.0-SNAPSHOT",
            VersionModifier.SNAPSHOT_APPENDER
    )
    val NUKKIT_X = NUKKIT
}

open class NukkitDebug(
        override var serverJar: File,
        override var serverDirectory: File,
        override var agentPort: Int
) : CommonDebug {
    @Inject
    constructor(serverJar: File) : this(serverJar, (serverJar), 5005)
}