import kr.entree.spigradle.build.VersionTask
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

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

val jacksonVersion = "2.18.3"
val kotlinVersion = "2.1.20"

dependencies {
    implementation(libs.kotlin.stdlib)
    implementation(libs.jackson.core)
    implementation(libs.jackson.annotations)
    implementation(libs.jackson.module.kotlin)
    implementation(libs.jackson.dataformat.yaml)
    implementation(libs.asm)
    implementation(libs.ideaExt)
    implementation(libs.downloadTask)
    implementation(libs.spigradleAnnotations)
    kapt(libs.autoService)
    compileOnly(libs.spigotApi)
    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testImplementation(libs.kotlin.test.junit5)
    testImplementation(gradleTestKit())
}

configurations {
    implementation.get().dependencies += kapt.get().dependencies
    testImplementation.get().dependencies += implementation.get().dependencies
}

kotlin {
    jvmToolchain(17)

    compilerOptions {
        // Set lower API and language version to make the plugins compatible with Gradle 8.0+
        // See: https://docs.gradle.org/current/userguide/compatibility.html#kotlin
        apiVersion = KotlinVersion.KOTLIN_1_8
        languageVersion = KotlinVersion.KOTLIN_1_8

        allWarningsAsErrors = true
    }
}

tasks {
    test {
        useJUnitPlatform()
        maxParallelForks = 4
        testLogging {
            events("passed", "skipped", "failed")
        }
        dependsOn(getByName("publishToMavenLocal"))
    }
}