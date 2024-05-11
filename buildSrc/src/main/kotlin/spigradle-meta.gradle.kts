@file:Suppress("UnstableApiUsage")

import kr.entree.spigradle.build.CodeGenerationTask


plugins {
    id("java")
}

// https://docs.gradle.org/current/userguide/validation_problems.html#implicit_dependency
val genDir = layout.buildDirectory.dir("generated/source/spigradle-build/main/java")
val generateSpigradleMeta by tasks.registering(CodeGenerationTask::class) {
    outputDir.convention(genDir)
}

tasks {
    classes {
        dependsOn(generateSpigradleMeta)
    }
}

sourceSets {
    main {
        java {
            srcDir(generateSpigradleMeta.flatMap { it.outputDir })
        }
    }
}
