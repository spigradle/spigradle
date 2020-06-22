@file:Suppress("UnstableApiUsage")

import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

plugins {
    kotlin("jvm")
}

val generatedSourceDir = File("$buildDir/generated/source/spigradle/kotlin/main")

sourceSets["main"].withConvention(KotlinSourceSet::class) {
    kotlin.srcDir(generatedSourceDir)
}

tasks {
    val metaFile = generatedSourceDir.resolve("kr/entree/spigradle/SpigradleMeta.kt")
    val generateSpigradleMeta by registering {
        group = "spigradle build"
        outputs.files(metaFile)
        doLast {
            metaFile.apply {
                parentFile.mkdirs()
            }.writeText("""
                package kr.entree.spigradle

                object SpigradleMeta {
                    const val VERSION = "${project.version}"
                }
            """.trimIndent())
        }
    }
    compileKotlin { dependsOn(generateSpigradleMeta) }
}