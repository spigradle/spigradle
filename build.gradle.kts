import org.gradle.kotlin.dsl.support.useToRun

plugins {
    val kotlinVersion = "1.3.72"
    kotlin("jvm") version kotlinVersion
    kotlin("kapt") version kotlinVersion
    `kotlin-dsl-base`
    groovy
    id("com.github.johnrengelman.shadow") version "5.2.0"
    id("com.gradle.plugin-publish") version "0.11.0" apply false
    id("com.jfrog.bintray") version "1.8.4" apply false
}

group = "kr.entree"
version = "1.3.0-SNAPSHOT"
description = "An intelligent Gradle plugin for developing Minecraft resources."

arrayOf("publish", "generateMeta").forEach { name ->
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
}

dependencies {
    val jacksonVersion = "2.11.0"
    shadow(gradleApi())
    shadow(localGroovy())
    shadow(kotlin("stdlib-jdk8"))
    shadow("com.google.guava:guava:29.0-jre")
    shadow("com.fasterxml.jackson.core:jackson-core:$jacksonVersion")
    shadow("com.fasterxml.jackson.core:jackson-annotations:$jacksonVersion")
    shadow("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    shadow("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:$jacksonVersion")
    kapt("com.google.auto.service:auto-service:1.0-rc7")
    implementation("org.ow2.asm:asm:8.0.1")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.2")
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit5"))
    testImplementation(gradleTestKit())
}

configurations {
    shadow.get().dependencies += kapt.get().dependencies
    testImplementation.get().dependencies += shadow.get().dependencies
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
    shadowJar {
        val packageName = "${project.group}.spigradle"
        relocate("org.objectweb.asm", "$packageName.lib.asm")
        archiveClassifier.set("")
        minimize()
    }
    jar {
        enabled = false
        finalizedBy(shadowJar)
    }
    val pluginUnderTestMetadata by registering {
        val testClasspath = sourceSets.test.get().compileClasspath
        File(temporaryDir, "plugin-under-test-metadata.properties").apply {
            parentFile.mkdirs()
        }.bufferedWriter().useToRun {
            write("implementation-classpath=")
            write(testClasspath.joinToString(File.pathSeparator) {
                it.absolutePath.replace("\\", "/")
            })
        }
    }
    test {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
        dependsOn(pluginUnderTestMetadata, getByName("publishToMavenLocal"))
    }
    afterEvaluate {
        val testMetadataFile = pluginUnderTestMetadata.get().temporaryDir
        dependencies.add(sourceSets.test.get().runtimeClasspathConfigurationName, files(testMetadataFile))
    }
}