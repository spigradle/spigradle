package kr.entree.spigradle.extension

import kr.entree.spigradle.util.ActualName
import kr.entree.spigradle.util.attr.Command
import kr.entree.spigradle.util.attr.Permission
import org.gradle.api.Action
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
    Property<String> main
    Property<String> name
    Property<String> version
    ListProperty<String> authors
    ListProperty<String> depend
    @ActualName('softdepend')
    ListProperty<String> softDepend
    @ActualName('loadbefore')
    ListProperty<String> loadBefore
    NamedDomainObjectContainer<Command> commands
    NamedDomainObjectContainer<Permission> permissions
    Property<String> description
    @ActualName('api-version')
    Property<String> apiVersion
    Property<String> load
    Property<String> website
    Property<String> prefix

    @Inject
    PluginAttributes(ObjectFactory factory, Project project) {
        main = factory.property(String)
        name = factory.property(String)
        version = factory.property(String)
        authors = factory.listProperty(String)
        depend = factory.listProperty(String)
        softDepend = factory.listProperty(String)
        loadBefore = factory.listProperty(String)
        commands = project.container(Command)
        permissions = project.container(Permission)
        description = factory.property(String)
        apiVersion = factory.property(String)
        load = factory.property(String)
        website = factory.property(String)
        prefix = factory.property(String)
    }

    def commands(Action<NamedDomainObjectContainer<Command>> configure) {
        configure.execute(commands)
    }

    def permissions(Action<NamedDomainObjectContainer<Permission>> configure) {
        configure.execute(permissions)
    }
}
