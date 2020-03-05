package kr.entree.spigradle

import kr.entree.spigradle.extension.PluginAttributesLegacy
import kr.entree.spigradle.extension.PluginAttributesModern
import kr.entree.spigradle.project.Dependency
import kr.entree.spigradle.project.Repositories
import kr.entree.spigradle.task.SpigotPluginYamlCreateTask
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.tasks.compile.AbstractCompile
import org.gradle.util.GradleVersion

import static kr.entree.spigradle.project.Dependencies.*

/**
 * Created by JunHyung Lim on 2019-12-13
 */
class SpigradleProject {
    final Project project

    SpigradleProject(project) {
        this.project = project
    }

    def setupPlugin() {
        def attrType = canSupportNamedDsl() ? PluginAttributesModern : PluginAttributesLegacy
        def attrs = project.extensions.create('spigot', attrType, project)
        def task = project.task('spigotPluginYaml', type: SpigotPluginYamlCreateTask) {
            group = 'Spigradle'
            description = 'Auto generate a plugin.yml file.'
            attributes = attrs
        }
        project.tasks.withType(Jar) {
            it.dependsOn task
        }
        def compileTasks = project.tasks.withType(AbstractCompile)
        if (!compileTasks.isEmpty()) {
            compileTasks.first()?.with {
                task.dependsOn it
            }
        }
    }

    def setupRepositories() {
        setupRepositories([
                'spigot'     : Repositories.SPIGOT,
                'bungeecord' : Repositories.BUNGEECORD,
                'paper'      : Repositories.PAPER,
                'protocolLib': Repositories.PROTOCOL_LIB,
                'jitpack'    : Repositories.JITPACK,
                'vault'      : Repositories.JITPACK,
                'enginehub'  : Repositories.ENGINEHUB,
                'codemc'     : Repositories.CODE_MC,
                'bStats'     : Repositories.CODE_MC
        ])
        project.repositories.with {
            spigot()
            bungeecord()
            paper()
        }
    }

    def setupDependencies() {
        setupDependencies([
                // Bukkit
                'spigot'         : SPIGOT,
                'spigotAll'      : SPIGOT_ALL,
                'minecraftServer': MINECRAFT_SERVER,
                'paper'          : PAPER,
                'bukkit'         : BUKKIT,
                'craftbukkit'    : CRAFT_BUKKIT,
                // Plugin
                'protocolLib'    : PROTOCOL_LIB,
                'vault'          : VAULT,
                'luckPerms'      : LUCK_PERMS,
                'worldedit'      : WORLD_EDIT,
                'worldguard'     : WORLD_GUARD,
                'commandhelper'  : COMMAND_HELPER,
                'bStats'         : BSTATS,
                'bStatsLite'     : BSTATS_LITE,
                'lombok'         : LOMBOK
        ])
    }

    def setupRepositories(Map<String, String> map) {
        def handler = project.repositories as RepositoryHandler
        map.each { entry ->
            def key = entry.key
            def address = entry.value
            handler.ext[key] = {
                handler.maven {
                    url = address
                }
            }
        }
    }

    def setupDependencies(Map<String, Object> map) {
        def handler = project.dependencies as DependencyHandler
        map.each {
            def value = it.value
            handler.ext[it.key] = value instanceof Dependency
                    ? value.formatter()
                    : { String version -> value.call(handler, version) }
        }
    }

    static boolean canSupportNamedDsl() {
        return GradleVersion.current() >= GradleVersion.version('6.0')
    }
}
