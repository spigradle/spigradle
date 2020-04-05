@file:Suppress("UnstableApiUsage")

plugins {
    id("groovy")
    id("org.jetbrains.kotlin.jvm") version "1.3.70"
    id("com.github.johnrengelman.shadow") version "5.2.0"
    id("com.gradle.plugin-publish") version "0.11.0" apply false
    id("com.jfrog.bintray") version "1.8.4" apply false
}

group = "kr.entree"
version = "1.2.4"
description = "Gradle plugin for developing Spigot plugin."

arrayOf("publish", "generateMeta").forEach {
    val buildFileName = "gradle/${it}.gradle"
    if (file(buildFileName).isFile) {
        apply(from = buildFileName)
    } else {
        apply(from = "${buildFileName}.kts")
    }
}

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    shadow(gradleApi())
    shadow(localGroovy())
    shadow("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.yaml:snakeyaml:1.25")
    implementation("org.ow2.asm:asm:7.2")
    testImplementation("junit:junit:4.12")
}

val packageName = "${project.group}.spigradle"

tasks {
    shadowJar {
        mapOf(
                "org.yaml.snakeyaml" to "snakeyaml",
                "org.objectweb.asm" to "asm"
        ).forEach { (start, alias) ->
            relocate(start, "${packageName}.libs.$alias")
        }
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
        archiveClassifier.set("")
        minimize()
    }
    jar {
        dependsOn(shadowJar)
        enabled = false
    }
    compileGroovy {
        classpath = sourceSets.main.get().compileClasspath
    }
    compileKotlin {
        classpath += files(sourceSets.main.get().withConvention(GroovySourceSet::class) { groovy }.classesDirectory)
    }
}