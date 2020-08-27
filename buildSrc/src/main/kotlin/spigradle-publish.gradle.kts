import groovy.lang.GroovyObject
import kr.entree.spigradle.build.*
import org.jfrog.gradle.plugin.artifactory.dsl.PublisherConfig
import java.util.*
import org.gradle.api.tasks.bundling.Jar

plugins {
    id("com.jfrog.bintray")
    id("com.jfrog.artifactory")
    id("com.gradle.plugin-publish")
    id("com.eden.orchidPlugin")
    `maven-publish`
}

val spigradleVcsUrl = "https://github.com/spigradle/spigradle.git"

bintray {
    user = findProperty("bintray.publish.user")?.toString()
    key = findProperty("bintray.publish.key")?.toString()
    setPublications("spigradle")
    publish = true
    pkg.apply {
        repo = "Spigradle"
        name = project.name
        desc = project.description
        websiteUrl = "https://github.com/spigradle/spigradle"
        githubRepo = "https://github.com/spigradle/spigradle"
        issueTrackerUrl = "https://github.com/spigradle/spigradle/issues"
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
            repoKey = if (version.toString().endsWith("-SNAPSHOT")) "oss-snapshot-local" else "oss-release-local"
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

val spigradleDocsJar by tasks.registering(Jar::class) {
    group = "spigradle build"
    archiveClassifier.set("docs")
    from("$projectDir/docs") {
        include("*.md")
    }
    from("$projectDir/CHANGELOG.md")
}

val spigradleSourcesJar by tasks.registering(Jar::class) {
    group = "spigradle build"
    archiveClassifier.set("sources")
    from(sourceSets["main"].allSource)
}

publishing {
    publications {
        create("spigradle", MavenPublication::class) {
            from(components["java"])
            artifact(spigradleDocsJar.get())
            artifact(spigradleSourcesJar.get())
        }
    }
}

pluginBundle {
    website = "https://github.com/spigradle/spigradle"
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

tasks.register<VersionTask>("setVersion")