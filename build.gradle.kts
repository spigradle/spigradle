plugins {
    val kotlinVersion = "1.3.72"
    kotlin("jvm") version kotlinVersion
    id("com.github.johnrengelman.shadow") version "5.2.0"
    id("com.gradle.plugin-publish") version "0.11.0" apply false
    id("com.jfrog.bintray") version "1.8.4" apply false
}

group = "kr.entree"
version = "1.3.0-SNAPSHOT"
description = "An intelligent Gradle plugin for developing Minecraft resources."

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
    ext
    "shadow"(gradleApi())
    val jacksonVersion = "2.11.0"
    shadow(localGroovy())
    shadow(gradleApi())
    shadow(kotlin("stdlib-jdk8"))
    shadow("com.google.guava", "guava", "29.0-jre")
    shadow("org.ow2.asm:asm:7.2")
    shadow("com.fasterxml.jackson.core:jackson-core:$jacksonVersion")
    shadow("com.fasterxml.jackson.core:jackson-annotations:$jacksonVersion")
    shadow("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    shadow("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:$jacksonVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.2")
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit5"))
    testImplementation(gradleTestKit())
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
    test {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }
}