@file:Suppress("UnstableApiUsage")

plugins {
    kotlin("jvm") version "1.3.72"
    groovy
    `java-gradle-plugin`
    id("com.gradle.plugin-publish") version "0.11.0" apply false
    id("com.jfrog.bintray") version "1.8.4" apply false
}

group = "kr.entree"
version = "1.3.0-SNAPSHOT"
description = "An intelligent Gradle plugin for developing Minecraft resources."

arrayOf("publish", "generateMeta").forEach {
    val buildFilePrefix = "gradle/${it}.gradle"
    val buildFileName = if (file(buildFilePrefix).isFile) buildFilePrefix else "$buildFilePrefix.kts"
    runCatching {
        apply(from = buildFileName)
    }.onFailure {
        throw GradleException("Error while evaluating $buildFileName")
    }
}

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    val jacksonVersion = "2.11.0"
    compileOnly(gradleApi())
    compileOnly(localGroovy())
    compileOnly(kotlin("stdlib-jdk8"))
    compileOnly("com.google.guava:guava:29.0-jre")
    compileOnly("org.ow2.asm:asm:7.2")
    compileOnly("com.fasterxml.jackson.core:jackson-core:$jacksonVersion")
    compileOnly("com.fasterxml.jackson.core:jackson-annotations:$jacksonVersion")
    compileOnly("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    compileOnly("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:$jacksonVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.2")
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit5"))
    testImplementation(gradleTestKit())
}

gradlePlugin {
    plugins {
        create("spigot") {
            id = "kr.entree.spigradle"
            implementationClass = "kr.entree.spigradle.module.spigot.SpigotPlugin"
        }
        create("nukkit") {
            id = "kr.entree.spigradle.nukkit"
            implementationClass = "kr.entree.spigradle.module.nukkit.NukkitPlugin"
        }
        create("bungeecord") {
            id = "kr.entree.spigradle.bungeecord"
            implementationClass = "kr.entree.spigradle.module.bungeecord.BungeecordPlugin"
        }
    }
}

configurations {
    testImplementation.get().dependencies += compileOnly.get().dependencies
    api.get().dependencies -= dependencies.gradleApi()
}

tasks {
    compileKotlin {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xopt-in=kotlin.RequiresOptIn")
        }
        classpath += files(sourceSets.main.get().withConvention(GroovySourceSet::class) { groovy }.classesDirectory)
    }
    compileGroovy {
        classpath = sourceSets.main.get().compileClasspath
    }
    test {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
        dependsOn(getByName("publishToMavenLocal"))
    }
    pluginUnderTestMetadata {
        pluginClasspath.from(sourceSets.test.get().compileClasspath)
    }
    groovydoc {
        enabled = false
    }
}