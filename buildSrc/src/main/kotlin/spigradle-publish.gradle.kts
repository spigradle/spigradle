import groovy.lang.GroovyObject
import kr.entree.spigradle.build.*
import java.util.*
import org.gradle.api.tasks.bundling.Jar

plugins {
    id("java")
    id("com.gradle.plugin-publish")
}

val spigradleVcsUrl = "https://github.com/spigradle/spigradle.git"

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

gradlePlugin {
    website = "https://github.com/spigradle/spigradle"
    vcsUrl = spigradleVcsUrl
    val baseTags = listOf("minecraft")
    plugins {
        create("spigradle") {
            id = "kr.entree.spigradle.base"
            implementationClass = "kr.entree.spigradle.module.common.SpigradlePlugin"
            tags = baseTags
        }
        create("spigot") {
            id = "kr.entree.spigradle"
            implementationClass = "kr.entree.spigradle.module.spigot.SpigotPlugin"
            tags = baseTags + listOf("paper", "spigot", "bukkit")
        }
        create("bungee") {
            id = "kr.entree.spigradle.bungee"
            implementationClass = "kr.entree.spigradle.module.bungee.BungeePlugin"
            tags = baseTags + listOf("bungeecord")
        }
        create("nukkit") {
            id = "kr.entree.spigradle.nukkit"
            implementationClass = "kr.entree.spigradle.module.nukkit.NukkitPlugin"
            tags = baseTags + listOf("nukkit", "nukkitX")
        }
    }
}

tasks.register<VersionTask>("setVersion")
