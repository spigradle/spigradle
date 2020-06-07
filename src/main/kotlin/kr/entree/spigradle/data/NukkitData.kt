package kr.entree.spigradle.data

import java.io.File

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
}

data class NukkitDebug(
        val nukkitJar: File,
        val nukkitDirectory: File = nukkitJar.parentFile,
        var agentPort: Int = 5005
)