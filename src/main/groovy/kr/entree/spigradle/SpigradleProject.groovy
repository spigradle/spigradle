package kr.entree.spigradle

import kr.entree.spigradle.extension.PluginAttributes
import kr.entree.spigradle.task.PluginYamlGenerater
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.plugins.JavaPlugin

/**
 * Created by JunHyung Lim on 2019-12-13
 */
class SpigradleProject {
    static String DEFAULT_SPIGOT_VERSION = '1.14.4-R0.1-SNAPSHOT'
    static String DEFAULT_PROTOCOL_LIB_VERSION = '4.4.0'
    static String DEFAULT_VAULT_VERSION = '1.7'
    static String DEFAULT_LUCKPERMS_VERSION = '4.4'
    final Project project

    SpigradleProject(project) {
        this.project = project
    }

    def setupPlugin() {
        def attrs = project.extensions.create('spigot', PluginAttributes)
        def task = project.task('createPluginYaml', type: PluginYamlGenerater) {
            group = 'Spigradle'
            description = 'Auto generate a plugin.yml file.'
            attributes = attrs
        }
        project.plugins.withType(JavaPlugin) {
            project.jar.dependsOn task
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
                DEFAULT_SPIGOT_VERSION,
                taggedVersionParser()
        )
        setupDependencies([
                'spigot'     : createDependency(
                        'org.spigotmc',
                        'spigot-api',
                        spigotVersionParser
                ),
                'paper'      : createDependency(
                        'com.destroystokyo.paper',
                        'paper-api',
                        spigotVersionParser
                ),
                'bukkit'     : createDependency(
                        'org.bukkit',
                        'bukkit',
                        spigotVersionParser
                ),
                'craftbukkit': createDependency(
                        'org.bukkit',
                        'craftbukkit',
                        spigotVersionParser
                ),
                'protocolLib': createDependency(
                        'com.comphenix.protocol',
                        'ProtocolLib',
                        versionParserWithDefault(DEFAULT_PROTOCOL_LIB_VERSION)
                ),
                'vault'      : createDependency(
                        'com.github.MilkBowl',
                        'VaultAPI',
                        versionParserWithDefault(DEFAULT_VAULT_VERSION)
                ),
                'luckPerms'  : createDependency(
                        'me.lucko.luckperms',
                        'luckperms-api',
                        versionParserWithDefault(DEFAULT_LUCKPERMS_VERSION)
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

    static Closure<String> versionParserWithDefault(String defaultVersion, Closure<String> parser = versionParser()) {
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
}
