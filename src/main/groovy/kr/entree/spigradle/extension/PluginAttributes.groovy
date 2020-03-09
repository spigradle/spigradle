package kr.entree.spigradle.extension

import kr.entree.spigradle.annotation.RenameTo
import kr.entree.spigradle.util.Enums
import kr.entree.spigradle.util.Version
import kr.entree.spigradle.util.attribute.Command
import kr.entree.spigradle.util.attribute.Load
import kr.entree.spigradle.util.attribute.Permission
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
    @RenameTo('api-version')
    String apiVersion
    Load load
    String prefix
    @RenameTo('depend')
    List<String> depends = new ArrayList<>()
    @RenameTo('softdepend')
    List<String> softDepends = new ArrayList<>()
    @RenameTo('loadbefore')
    List<String> loadBefore = new ArrayList<>()
    final NamedDomainObjectContainer<Command> commands
    final NamedDomainObjectContainer<Permission> permissions
    static Load POST_WORLD = Load.POST_WORLD
    static Load STARTUP = Load.STARTUP

    PluginAttributes(NamedDomainObjectContainer<Command> commands, NamedDomainObjectContainer<Permission> permissions) {
        this.commands = commands
        this.permissions = permissions
    }

    def setLoad(Load load) {
        this.load = load
    }

    def setLoad(String name) {
        load = Enums.get(Load, name)
    }

    def setApiVersion(Version apiVersion) {
        this.apiVersion = apiVersion.toString()
    }

    def setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion
    }
}
