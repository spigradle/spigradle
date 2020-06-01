package kr.entree.spigradle.module.spigot

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import groovy.lang.Closure
import kr.entree.spigradle.data.Command
import kr.entree.spigradle.data.Load
import kr.entree.spigradle.data.Permission
import kr.entree.spigradle.data.FileEntry
import kr.entree.spigradle.internal.SerialName
import kr.entree.spigradle.internal.StandardDescription
import kr.entree.spigradle.internal.Transient
import kr.entree.spigradle.module.common.debugDir
import kr.entree.spigradle.module.common.spigotBuildToolDir
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import java.io.File

@JsonPropertyOrder(
        "main", "name", "version", "description", "website",
        "authors", "api-version", "load", "prefix", "depend",
        "softdepend", "loadbefore", "commands", "permissions"
)
open class SpigotDescription(project: Project) : StandardDescription {
    override var main: String? = null
    override var name: String? = null
    override var version: String? = null
    var description: String? = null
    var website: String? = null
    var authors: List<String> = emptyList()

    @SerialName("api-version")
    var apiVersion: String? = null
    var load: Load? = null
    var prefix: String? = null

    @SerialName("depend")
    var depends: List<String> = emptyList()

    @SerialName("softdepend")
    var softDepends: List<String> = emptyList()

    @SerialName("loadbefore")
    var loadBefore: List<String> = emptyList()

    val commands: NamedDomainObjectContainer<Command> = project.container(Command::class.java)
    val permissions: NamedDomainObjectContainer<Permission> = project.container(Permission::class.java)

    @Transient
    val debug: SpigotDebug = SpigotDebug(
            FileEntry(File(project.debugDir, "spigot/spigot.jar")),
            FileEntry(File(project.debugDir, "paper/paper.jar")),
            FileEntry(File(project.gradle.spigotBuildToolDir, "BuildTools.jar"))
    )

    fun debug(configure: Closure<*>) {
        configure.delegate = debug
        configure.call()
    }

    fun debug(configure: SpigotDebug.() -> Unit) = configure(debug)
}