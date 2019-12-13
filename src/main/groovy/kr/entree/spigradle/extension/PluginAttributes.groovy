package kr.entree.spigradle.extension

import kr.entree.spigradle.util.attr.Command
import kr.entree.spigradle.util.attr.Load
import kr.entree.spigradle.util.attr.Permission
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property

import javax.inject.Inject

/**
 * Created by JunHyung Lim on 2019-12-12
 */
class PluginAttributes {
    final Property<String> main
    final Property<String> name
    final Property<String> version
    final ListProperty<String> authors
    final ListProperty<String> depend
    final ListProperty<String> softDepend
    final ListProperty<String> loadBefore
    final NamedDomainObjectContainer<Command> commands
    final NamedDomainObjectContainer<Permission> permissions
    final Property<String> description
    final Property<String> apiVersion
    final Property<Load> load
    final Property<String> website
    final Property<String> prefix

    @Inject
    PluginAttributes(ObjectFactory factory) {
        main = factory.property(String)
        name = factory.property(String)
        version = factory.property(String)
        authors = factory.listProperty(String)
        depend = factory.listProperty(String)
        softDepend = factory.listProperty(String)
        loadBefore = factory.listProperty(String)
        commands = factory.domainObjectContainer(Command)
        permissions = factory.domainObjectContainer(Permission)
        description = factory.property(String)
        apiVersion = factory.property(String)
        load = factory.property(Load)
        website = factory.property(String)
        prefix = factory.property(String)
    }
}
