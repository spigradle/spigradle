import groovy.lang.GroovyObject
import kr.entree.spigradle.build.*
import org.gradle.jvm.tasks.Jar
import org.jfrog.gradle.plugin.artifactory.dsl.PublisherConfig
import java.util.*

plugins {
    id("com.jfrog.bintray")
    id("com.jfrog.artifactory")
    id("com.gradle.plugin-publish")
    id("com.eden.orchidPlugin")
    `maven-publish`
}

val spigradleVcsUrl = "https://github.com/EntryPointKR/Spigradle.git"

bintray {
    user = findProperty("bintray.publish.user")?.toString()
    key = findProperty("bintray.publish.key")?.toString()
    setPublications("spigradle")
    publish = true
    pkg.apply {
        repo = "Spigradle"
        name = project.name
        desc = project.description
        websiteUrl = "https://github.com/EntryPointKR/Spigradle"
        githubRepo = "https://github.com/EntryPointKR/Spigradle"
        issueTrackerUrl = "https://github.com/EntryPointKR/Spigradle/issues"
        setLicenses("Apache-2.0")
        vcsUrl = spigradleVcsUrl
    }
    project.afterEvaluate {
        pkg.version.apply {
            name = project.version.toString()
            released = Date().toString()
            vcsTag = project.version.toString()
        }
    }
}

artifactory {
    setContextUrl("https://oss.jfrog.org/artifactory")
    publish(closureOf<PublisherConfig> {
        repository(closureOf<GroovyObject> {
            repoKey = "oss-snapshot-local"
            username = findProperty("bintray.publish.user")
            password = findProperty("bintray.publish.key")
        })
        defaults(closureOf<GroovyObject> {
            publications("spigradle")
            publishArtifacts = true
            publishPom = true
        })
    })
    resolve(closureOf<GroovyObject> {
        repoKey = "jcenter"
    })
    clientConfig.info.buildNumber = findProperty("build.number")?.toString()
}

publishing {
    publications {
        create("spigradle", MavenPublication::class) {
            from(components["java"])
            afterEvaluate {
                artifact(tasks.getByName<Jar>("kotlinSourcesJar"))
            }
        }
    }
}

pluginBundle {
    website = "https://github.com/EntryPointKR/Spigradle"
    vcsUrl = spigradleVcsUrl
    tags = listOf("minecraft", "paper", "spigot", "bukkit", "bungeecord", "nukkit", "nukkitX")
    fun formatDesc(name: String) = "An intelligent Gradle plugin for developing $name plugin."
    plugins {
        create("spigradle") {
            displayName = "Spigradle Base Plugin"
            description = "The base plugin of Spigradle"
        }
        create("spigot") {
            displayName = "Spigradle Spigot Plugin"
            description = formatDesc("Spigot")
        }
        create("bungee") {
            displayName = "Spigradle Bungeecord Plugin"
            description = formatDesc("Bungeecord")
        }
        create("nukkit") {
            displayName = "Spigradle NukkitX Plugin"
            description = formatDesc("NukkitX")
        }
    }
}