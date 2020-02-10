package kr.entree.spigradle.extension

import kr.entree.spigradle.util.Enums
import kr.entree.spigradle.util.annotation.ActualName
import kr.entree.spigradle.util.annotation.MappingObject
import kr.entree.spigradle.util.attr.Command
import kr.entree.spigradle.util.attr.Load
import kr.entree.spigradle.util.attr.Permission
import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project

import javax.inject.Inject

/**
 * Created by JunHyung Lim on 2019-12-12
 */
@MappingObject
class PluginAttributes {
    String main
    String name
    String version
    String description
    String website
    List<String> authors
    @ActualName('api-version')
    String apiVersion
    Load load
    String prefix
    @ActualName('depend')
    List<String> depends = new ArrayList<>()
    @ActualName('softdepend')
    List<String> softDepends = new ArrayList<>()
    @ActualName('loadbefore')
    List<String> loadBefore = new ArrayList<>()
    NamedDomainObjectContainer<Command> commands
    NamedDomainObjectContainer<Permission> permissions
    static Load POST_WORLD = Load.POST_WORLD
    static Load STARTUP = Load.STARTUP

    @Inject
    PluginAttributes(Project project) {
        commands = project.container(Command)
        permissions = project.container(Permission)
    }

    def commands(Action<NamedDomainObjectContainer<Command>> configure) {
        configure.execute(commands)
    }

    def permissions(Action<NamedDomainObjectContainer<Permission>> configure) {
        configure.execute(permissions)
    }

    def setLoad(String name) {
        load = Enums.get(Load, name)
    }
}
