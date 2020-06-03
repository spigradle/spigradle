package kr.entree.spigradle.module.spigot

import kr.entree.spigradle.data.FileEntry
import java.io.File

data class SpigotDebug(
        var serverJar: File,
        var buildToolJar: File,
        var serverDirectory: File = serverJar.parentFile,
        var buildToolDirectory: File = buildToolJar.parentFile,
        var buildToolOutputDirectory: File = File(buildToolDirectory, "outputs"),
        var agentPort: Int = 5005,
        var eula: Boolean = false,
        var buildVersion: String = "1.15.2"
)