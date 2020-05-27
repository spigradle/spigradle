package kr.entree.spigradle.module.nukkit

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import kr.entree.spigradle.data.Command
import kr.entree.spigradle.data.Load
import kr.entree.spigradle.data.Permission
import kr.entree.spigradle.internal.SerialName
import kr.entree.spigradle.internal.StandardDescription
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.kotlin.dsl.container

/**
 * Created by JunHyung Lim on 2020-05-22
 */
@JsonPropertyOrder(
        "main", "name", "version", "description", "website",
        "authors", "api", "load", "prefix", "depend",
        "softdepend", "loadbefore", "commands", "permissions"
)
open class NukkitDescription(project: Project) : StandardDescription {
    override var main: String? = null
    override var name: String? = null
    override var version: String? = null
    var description: String? = null
    var website: String? = null
    var authors: List<String> = emptyList()
    var api: List<String> = emptyList()
    var load: Load? = null
    var prefix: String? = null

    @SerialName("depend")
    var depends: List<String> = emptyList()

    @SerialName("softdepend")
    var softDepends: List<String> = emptyList()

    @SerialName("loadbefore")
    var loadBefore: List<String> = emptyList()

    val commands: NamedDomainObjectContainer<Command> = project.container(Command::class)
    val permissions: NamedDomainObjectContainer<Permission> = project.container(Permission::class)
}