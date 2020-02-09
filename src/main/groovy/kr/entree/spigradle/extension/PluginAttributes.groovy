package kr.entree.spigradle.extension

import kr.entree.spigradle.util.Enums
import kr.entree.spigradle.util.annotation.ActualName
import kr.entree.spigradle.util.attr.Command
import kr.entree.spigradle.util.attr.Load
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
    Property<String> description
    Property<String> website
    ListProperty<String> authors
    Property<Load> load
    @ActualName('depend')
    ListProperty<String> depends
    @ActualName('softdepend')
    ListProperty<String> softDepends
    @ActualName('loadbefore')
    ListProperty<String> loadBefore
    @ActualName('api-version')
    Property<String> apiVersion
    Property<String> prefix
    NamedDomainObjectContainer<Command> commands
    NamedDomainObjectContainer<Permission> permissions
    static Load POST_WORLD = Load.POST_WORLD
    static Load STARTUP = Load.STARTUP

    @Inject
    PluginAttributes(ObjectFactory factory, Project project) {
        main = factory.property(String)
        name = factory.property(String)
        version = factory.property(String)
        authors = factory.listProperty(String)
        depends = factory.listProperty(String)
        softDepends = factory.listProperty(String)
        loadBefore = factory.listProperty(String)
        commands = project.container(Command)
        permissions = project.container(Permission)
        description = factory.property(String)
        apiVersion = factory.property(String)
        load = factory.property(Load)
        website = factory.property(String)
        prefix = factory.property(String)
    }

    def commands(Action<NamedDomainObjectContainer<Command>> configure) {
        configure.execute(commands)
    }

    def permissions(Action<NamedDomainObjectContainer<Permission>> configure) {
        configure.execute(permissions)
    }

    def setLoad(String name) {
        load.set(Enums.get(Load, name))
    }
}
