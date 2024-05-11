import kr.entree.spigradle.build.VersionTask

plugins {
    id("java")
    id("com.gradle.plugin-publish")
}

val spigradleVcsUrl = "https://github.com/spigradle/spigradle.git"

java {
    withSourcesJar()
    withJavadocJar()
}

tasks {
    named<Jar>("javadocJar") {
        from("$projectDir/docs") {
            include("*.md")
        }
        from("$projectDir/CHANGELOG.md")
    }
}

publishing {
    publications {
        create("spigradle", MavenPublication::class) {
            from(components["java"])
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
