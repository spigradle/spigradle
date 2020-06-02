package kr.entree.spigradle.module.spigot

import groovy.lang.Closure
import kr.entree.spigradle.data.FileEntry
import java.io.File

data class SpigotDebug(
        val spigotFile: FileEntry,
        val paperFile: FileEntry,
        var buildToolFile: FileEntry,
        var buildToolOutputDirectory: File = File(buildToolFile.directory, "outputs"),
        var agentPort: Int = 5005,
        var eula: Boolean = false,
        var buildVersion: String = "1.15.2"
) {
    fun spigot(configure: FileEntry.() -> Unit) = spigotFile.run(configure)

    fun spigot(configure: Closure<*>) {
        configure.delegate = spigotFile
        configure.call()
    }

    fun paper(configure: FileEntry.() -> Unit) = paperFile.run(configure)

    fun paper(configure: Closure<*>) {
        configure.delegate = paperFile
        configure.call()
    }
}