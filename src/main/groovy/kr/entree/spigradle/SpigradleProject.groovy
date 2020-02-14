package kr.entree.spigradle

import kr.entree.spigradle.extension.PluginAttributesLegacy
import kr.entree.spigradle.extension.PluginAttributesModern
import kr.entree.spigradle.task.SpigotPluginYamlCreateTask
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.tasks.compile.AbstractCompile
import org.gradle.util.GradleVersion

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
        project.tasks.withType(AbstractCompile).first()?.with {
            task.dependsOn it
        }
    }

    def setupRepositories() {
        def jitPack = [
                'jitpack-repo': 'https://jitpack.io'
        ]
        setupRepositories([
                'spigot'     : [
                        'spigot-repo': 'https://hub.spigotmc.org/nexus/content/repositories/snapshots/'
                ],
                'bungeecord' : [
                        'bungeecord-repo': 'https://oss.sonatype.org/content/repositories/snapshots'
                ],
                'paper'      : [
                        'paper-repo': 'https://papermc.io/repo/repository/maven-public/'
                ],
                'protocolLib': [
                        'protocollib-repo': 'https://repo.dmulloy2.net/nexus/repository/public/'
                ],
                'jitpack'    : jitPack,
                'vault'      : jitPack,
                'enginehub'  : [
                        'enginehub-repo': 'https://maven.enginehub.org/repo/'
                ]
        ])
        project.repositories.with {
            spigot()
            bungeecord()
            paper()
        }
    }

    def setupDependencies() {
        def spigotVersionParser = versionParserWithDefault(
                '1.14.4-R0.1-SNAPSHOT',
                taggedVersionParser()
        )
        setupDependencies([
                'spigot'       : createDependency(
                        'org.spigotmc',
                        'spigot-api',
                        spigotVersionParser
                ),
                'paper'        : createDependency(
                        'com.destroystokyo.paper',
                        'paper-api',
                        spigotVersionParser
                ),
                'bukkit'       : createDependency(
                        'org.bukkit',
                        'bukkit',
                        spigotVersionParser
                ),
                'craftbukkit'  : createDependency(
                        'org.bukkit',
                        'craftbukkit',
                        spigotVersionParser
                ),
                'protocolLib'  : createDependency(
                        'com.comphenix.protocol',
                        'ProtocolLib',
                        versionParserWithDefault('4.4.0')
                ),
                'vault'        : createDependency(
                        'com.github.MilkBowl',
                        'VaultAPI',
                        versionParserWithDefault('1.7')
                ),
                'luckPerms'    : createDependency(
                        'me.lucko.luckperms',
                        'luckperms-api',
                        versionParserWithDefault('5.0')
                ),
                'worldedit'    : createDependency(
                        'com.sk89q.worldedit',
                        'worldedit-bukkit',
                        versionParserWithDefault('7.1.0')
                ),
                'worldguard'   : createDependency(
                        'com.sk89q.worldguard',
                        'worldguard-bukkit',
                        versionParserWithDefault('7.0.2')
                ),
                'commandhelper': createDependency(
                        'com.sk89q',
                        'commandhelper',
                        versionParserWithDefault('3.3.4-SNAPSHOT')
                )
        ])
    }

    def setupRepositories(Map<String, Map<String, String>> map) {
        def handler = project.repositories as RepositoryHandler
        map.each { entry ->
            handler.ext[entry.key] = {
                entry.value.each { repos ->
                    handler.maven {
                        name = repos.key
                        url = repos.value
                    }
                }
            }
        }
    }

    def setupDependencies(Map<String, Closure> map) {
        def handler = project.dependencies as DependencyHandler
        map.each {
            handler.ext[it.key] = { Object... args ->
                return it.value.call(handler, args)
            }
        }
    }

    static Closure createDependency(String group, String artifact, Closure<String> versionParser) {
        return { DependencyHandler handler, Object... args ->
            handler.create("$group:$artifact:${versionParser.call(args)}")
        }
    }

    static Closure<String> versionParser() {
        return { Object... args ->
            return args.join('.')
        }
    }

    static Closure<String> versionParserWithDefault(String defaultVersion = '+', Closure<String> parser = versionParser()) {
        return { Object... args ->
            if (args.length <= 0) {
                return defaultVersion
            }
            return parser.call(args)
        }
    }

    static Closure<String> taggedVersionParser() {
        return { Object... args ->
            def builder = new StringBuilder()
            if (args.length == 1 && args[0] instanceof CharSequence) {
                builder.append(args[0].toString())
            } else if (args.length > 1) {
                builder.append(versionParser().call(args))
            }
            if (builder.length() > 0) {
                def separators = builder.count('-')
                if (separators <= 0) {
                    builder.append('-R0.1')
                    separators++
                }
                if (separators >= 1) {
                    builder.append('-SNAPSHOT')
                }
                return builder.toString()
            }
            return '+'
        }
    }

    static boolean canSupportNamedDsl() {
        return GradleVersion.current() >= GradleVersion.version('6.0')
    }
}
