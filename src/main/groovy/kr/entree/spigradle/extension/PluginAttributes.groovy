package kr.entree.spigradle.extension

import kr.entree.spigradle.util.Enums
import kr.entree.spigradle.util.annotation.ActualName
import kr.entree.spigradle.util.attr.Command
import kr.entree.spigradle.util.attr.Load
import kr.entree.spigradle.util.attr.Permission
import org.gradle.api.NamedDomainObjectContainer

/**
 * Created by JunHyung Lim on 2019-12-12
 */
abstract class PluginAttributes {
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
    final NamedDomainObjectContainer<Command> commands
    final NamedDomainObjectContainer<Permission> permissions
    static Load POST_WORLD = Load.POST_WORLD
    static Load STARTUP = Load.STARTUP

    PluginAttributes(NamedDomainObjectContainer<Command> commands, NamedDomainObjectContainer<Permission> permissions) {
        this.commands = commands
        this.permissions = permissions
    }

    def setLoad(String name) {
        load = Enums.get(Load, name)
    }
}
