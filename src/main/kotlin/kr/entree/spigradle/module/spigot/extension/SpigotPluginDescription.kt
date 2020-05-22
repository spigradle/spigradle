package kr.entree.spigradle.module.spigot.extension

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import kr.entree.spigradle.data.Command
import kr.entree.spigradle.data.Load
import kr.entree.spigradle.data.Permission
import kr.entree.spigradle.internal.MainProvider
import kr.entree.spigradle.internal.SerialName
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project

@JsonPropertyOrder(
        "main", "name", "version", "description", "website",
        "authors", "api-version", "load", "prefix", "depend",
        "softdepend", "loadbefore", "commands", "permissions"
)
open class SpigotPluginDescription(project: Project) : MainProvider {
    init {
        project.afterEvaluate {
            setDefaults(this)
        }
    }

    override var main: String? = null
    var name: String? = null
    var version: String? = null
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

    fun setDefaults(project: Project) {
        name = name ?: project.name
        version = version ?: project.version.toString()
    }
}