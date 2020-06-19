plugins {
    val kotlinVersion = "1.3.72"
    kotlin("jvm") version kotlinVersion
    kotlin("kapt") version kotlinVersion
    groovy
    `kotlin-dsl-base`
    `java-gradle-plugin`
    id("com.gradle.plugin-publish") version "0.11.0" apply false
    id("com.jfrog.bintray") version "1.8.5" apply false
    id("com.jfrog.artifactory") version "4.15.2" apply false
    id("org.jetbrains.dokka") version "0.10.0" apply false
    id("com.eden.orchidPlugin") version "0.21.0" apply false
}

group = "kr.entree"
version = "1.3.0-SNAPSHOT"
description = "An intelligent Gradle plugin for developing Minecraft resources."

arrayOf("publish", "generateMeta", "docs").forEach { name ->
    val buildFilePrefix = "gradle/${name}.gradle"
    val buildFileName = if (file(buildFilePrefix).isFile) buildFilePrefix else "$buildFilePrefix.kts"
    runCatching {
        apply(from = buildFileName)
    }.onFailure {
        throw GradleException("Error while evaluating $buildFileName", it)
    }
}

repositories {
    mavenCentral()
    jcenter()
    gradlePluginPortal()
    maven { setUrl("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") }
    maven { setUrl("https://oss.sonatype.org/content/repositories/snapshots/") }
}

dependencies {
    val jacksonVersion = "2.11.0"
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.google.guava:guava:29.0-jre")
    implementation("com.fasterxml.jackson.core:jackson-core:$jacksonVersion")
    implementation("com.fasterxml.jackson.core:jackson-annotations:$jacksonVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:$jacksonVersion")
    implementation("org.ow2.asm:asm:8.0.1")
    implementation("gradle.plugin.org.jetbrains.gradle.plugin.idea-ext:gradle-idea-ext:0.7")
    kapt("com.google.auto.service:auto-service:1.0-rc7")
    compileOnly("org.spigotmc:spigot-api:1.15.2-R0.1-SNAPSHOT")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.2")
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit5"))
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
        @Suppress("UnstableApiUsage")
        classpath += files(sourceSets.main.get().withConvention(GroovySourceSet::class) { groovy }.classesDirectory)
    }
    compileGroovy {
        classpath = sourceSets.main.get().compileClasspath
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