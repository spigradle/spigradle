package kr.entree.spigradle.module.spigot.extension

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import kr.entree.spigradle.internal.NamedDomainObjectContainerSerializer
import kr.entree.spigradle.internal.SerialName
import kr.entree.spigradle.internal.Serialize
import kr.entree.spigradle.module.spigot.data.Command
import kr.entree.spigradle.module.spigot.data.Load
import kr.entree.spigradle.module.spigot.data.Permission
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project

@JsonPropertyOrder(
        "main", "name", "version", "description", "website",
        "authors", "api-version", "load", "prefix", "depend",
        "softdepend", "loadbefore", "commands", "permissions"
)
open class SpigotPluginAttributes {
    var main: String? = null
    var name: String? = null
    var version: String? = null
    var description: String? = null
    var website: String? = null
    var authors: List<String> = emptyList()

    @SerialName("api-version")
    var apiVersion: String? = null
    var load: Load? = null
    var prefix: String? = null

    @SerialName("depend") // Sync format style?
    var depends: List<String> = emptyList()

    @SerialName("softdepend")
    var softDepends: List<String> = emptyList()

    @SerialName("loadbefore")
    var loadBefore: List<String> = emptyList()

    @Serialize(using = NamedDomainObjectContainerSerializer::class)
    lateinit var commands: NamedDomainObjectContainer<Command>

    @Serialize(using = NamedDomainObjectContainerSerializer::class)
    lateinit var permissions: NamedDomainObjectContainer<Permission>

    internal fun init(project: Project) {
        commands = project.container(Command::class.java)
        permissions = project.container(Permission::class.java)
    }
}