@file:UseSerializers(NamedDomainObjectContainerSerializer::class)

package kr.entree.spigradle.spigot.extension

import com.charleskorn.kaml.Yaml
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import kr.entree.spigradle.util.NamedDomainObjectContainerSerializer
import kr.entree.spigradle.spigot.data.Command
import kr.entree.spigradle.spigot.data.Load
import kr.entree.spigradle.spigot.data.Permission
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.testfixtures.ProjectBuilder

@Serializable
open class SpigotPluginDescriptor {
    var main: String? = null
    var name: String? = null
    var version: String? = null
    var description: String? = null
    var website: String? = null
    var authors: String? = null
    var apiVersion: String? = null
    var load: Load? = null
    @SerialName("depend") // Sync format style?
    var depends: List<String> = emptyList()
    @SerialName("softDepend")
    var softDepends: List<String> = emptyList()
    var loadBefore: List<String> = emptyList()
    lateinit var commands: NamedDomainObjectContainer<Command>
    lateinit var permissions: NamedDomainObjectContainer<Permission>
}

fun main() {
    val project = ProjectBuilder.builder().build()
    val descriptor = SpigotPluginDescriptor().apply {
        commands = project.container(Command::class.java)
        permissions = project.container(Permission::class.java)
    }
    descriptor.commands.apply {
        create("awef") {

        }
    }
    println(Yaml.default.stringify(SpigotPluginDescriptor.serializer(), descriptor))
}