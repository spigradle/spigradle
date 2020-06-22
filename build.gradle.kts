plugins {
    kotlin("jvm")
    kotlin("kapt")
    groovy
    `kotlin-dsl-base`
    `java-gradle-plugin`
    `spigradle-meta`
    `spigradle-publish`
    `spigradle-docs`
}

group = "kr.entree"
version = "1.3.0-SNAPSHOT"
description = "An intelligent Gradle plugin for developing Minecraft resources."

gradlePlugin {
    plugins {
        create("spigradle") {
            id = "kr.entree.spigradle.base"
            implementationClass = "kr.entree.spigradle.module.common.SpigradlePlugin"
        }
        create("spigot") {
            id = "kr.entree.spigradle"
            implementationClass = "kr.entree.spigradle.module.spigot.SpigotPlugin"
        }
        create("bungee") {
            id = "kr.entree.spigradle.bungee"
            implementationClass = "kr.entree.spigradle.module.bungee.BungeePlugin"
        }
        create("nukkit") {
            id = "kr.entree.spigradle.nukkit"
            implementationClass = "kr.entree.spigradle.module.nukkit.NukkitPlugin"
        }
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