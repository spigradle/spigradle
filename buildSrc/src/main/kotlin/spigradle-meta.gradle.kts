@file:Suppress("UnstableApiUsage")

import kr.entree.spigradle.build.CodeGenerationTask


plugins {
    id("java")
}

val defaultGenDir = layout.buildDirectory.dir("generated/source/spigradle-build/main/java")
val generateSpigradleMeta by tasks.registering(CodeGenerationTask::class) {
    version.convention(project.version.toString())
    outputDir.convention(defaultGenDir)
}

tasks {
    classes {
        dependsOn(generateSpigradleMeta)
    }
}

sourceSets {
    main {
        java {
            // https://docs.gradle.org/current/userguide/validation_problems.html#implicit_dependency
            val genDir = generateSpigradleMeta.flatMap { it.outputDir }
            srcDir(genDir)
        }
    }
}
