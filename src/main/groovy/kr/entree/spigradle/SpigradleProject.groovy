package kr.entree.spigradle

import kr.entree.spigradle.extension.PluginAttributes
import kr.entree.spigradle.task.PluginYamlGenerater
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.internal.artifacts.dsl.DefaultRepositoryHandler
import org.gradle.api.internal.artifacts.dsl.dependencies.DefaultDependencyHandler

/**
 * Created by JunHyung Lim on 2019-12-13
 */
class SpigradleProject {
    static String DEFAULT_SPIGOT_VERSION = '1.14.4-R0.1-SNAPSHOT'
    static String DEFAULT_PROTOCOL_LIB_VERSION = '4.4.0'
    static String DEFAULT_VAULT_VERSION = '1.7'
    final Project project

    SpigradleProject(project) {
        this.project = project
    }

    def setupPlugin() {
        def attributes = project.extensions.create('spigot', PluginAttributes)
        def task = project.task('createPluginYaml', type: PluginYamlGenerater) {
            group = 'Spigradle'
            description = 'Auto generate a plugin.yml file.'
            attr = attributes
        }
        project.jar.dependsOn task
    }

    def setupRepositories() {
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
                        'protocollib-repo': 'http://repo.dmulloy2.net/nexus/repository/public/'
                ],
                'jitpack'    : [
                        'jitpack.io': 'https://jitpack.io'
                ]
        ])
        project.repositories.with {
            spigot()
            bungeecord()
            paper()
            protocolLib()
            jitpack()
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
                'protocolLib': createDependency(
                        'com.comphenix.protocol',
                        'ProtocolLib',
                        versionParserWithDefault(DEFAULT_PROTOCOL_LIB_VERSION)
                ),
                'vault'      : createDependency(
                        'com.github.MilkBowl',
                        'VaultAPI',
                        versionParserWithDefault(DEFAULT_VAULT_VERSION)
                )
        ])
    }

    def setupRepositories(Map<String, Map<String, String>> map) {
        def handler = project.repositories as DefaultRepositoryHandler
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
        def handler = project.dependencies as DefaultDependencyHandler
        map.each {
            handler.ext[it.key] = { Object... args ->
                return it.value.call(handler, args)
            }
        }
    }

    static Closure createDependency(String group, String artifact, Closure<String> versionParser) {
        return { DependencyHandler handler, Object... args ->
            handler.compileOnly "$group:$artifact:${versionParser.call(args)}"
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
