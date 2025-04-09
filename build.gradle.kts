import kr.entree.spigradle.build.VersionTask

plugins {
    `kotlin-dsl`
    `kotlin-kapt`
    `java-gradle-plugin`
    `spigradle-meta`
    `spigradle-publish`
    `spigradle-docs`
}

group = "kr.entree"
version = VersionTask.readVersion(project)
description = "An intelligent Gradle plugin for developing Minecraft resources."

repositories {
    mavenCentral()
    gradlePluginPortal()
    maven { setUrl("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") }
    maven { setUrl("https://oss.sonatype.org/content/repositories/snapshots/") }
}

val jacksonVersion = "2.12.7"
val kotlinVersion = "1.5.21"

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${kotlinVersion}")
    implementation("com.google.guava:guava:31.0.1-jre")
    implementation("com.fasterxml.jackson.core:jackson-core:$jacksonVersion")
    implementation("com.fasterxml.jackson.core:jackson-annotations:$jacksonVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:$jacksonVersion")
    implementation("org.ow2.asm:asm:9.2")
    implementation("gradle.plugin.org.jetbrains.gradle.plugin.idea-ext:gradle-idea-ext:0.8.1")
    implementation("de.undercouch:gradle-download-task:4.1.2")
    implementation("kr.entree:spigradle-annotations:2.2.0")
    kapt("com.google.auto.service:auto-service:1.0.1")
    compileOnly("org.spigotmc:spigot-api:1.15.2-R0.1-SNAPSHOT")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
    testImplementation("org.jetbrains.kotlin:kotlin-test:${kotlinVersion}")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:${kotlinVersion}")
    testImplementation(gradleTestKit())
}

configurations {
    implementation.get().dependencies += kapt.get().dependencies
    testImplementation.get().dependencies += implementation.get().dependencies
}

tasks {
    compileKotlin {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xopt-in=kotlin.RequiresOptIn")
        }
    }
    test {
        useJUnitPlatform()
        maxParallelForks = 4
        testLogging {
            events("passed", "skipped", "failed")
        }
        dependsOn(getByName("publishToMavenLocal"))
    }
}