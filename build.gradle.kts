plugins {
    val kotlinVersion = "1.3.72"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion
    id("com.github.johnrengelman.shadow") version "5.2.0"
    id("com.gradle.plugin-publish") version "0.11.0" apply false
    id("com.jfrog.bintray") version "1.8.4" apply false
}

group = "kr.entree"
version = "1.3.0-SNAPSHOT"
description = "An smart Gradle plugin for developing Minecraft resources."

arrayOf("publish", "generateMeta").forEach {
    val buildFileName = "gradle/${it}.gradle"
    if (file(buildFileName).isFile) {
        apply(from = buildFileName)
    } else {
        apply(from = "${buildFileName}.kts")
    }
}

repositories {
    ext
    mavenCentral()
    jcenter()
}

fun ExternalModuleDependency.excludeStdlib() {
    exclude(module = "kotlin-stdlib")
    exclude(module = "kotlin-stdlib-jdk7")
    exclude(module = "kotlin-stdlib-jdk8")
    exclude(module = "kotlin-stdlib-common")
}

dependencies {
    shadow(gradleApi())
    shadow(kotlin("stdlib-jdk8"))
    shadow("com.google.guava", "guava", "29.0-jre")
    implementation("org.ow2.asm:asm:7.2")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.20.0") {
        excludeStdlib()
    }
    implementation("com.charleskorn.kaml:kaml:0.17.0") {
        excludeStdlib()
    }
    testImplementation("junit:junit:4.12")
}

configurations {
    testImplementation.get().dependencies += shadow.get().dependencies
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
    }
    jar {
        dependsOn(shadowJar)
        enabled = false
    }
    compileKotlin {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xopt-in=kotlin.RequiresOptIn")
        }
    }
}