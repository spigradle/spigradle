package kr.entree.spigradle.internal

import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskContainer
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.withType

internal inline fun <T> notNull(any: T?, message: () -> String = { "" }): T {
    return any ?: throw GradleException(message())
}

internal fun TaskContainer.findArtifactJar() =
        withType<Jar>().mapNotNull {
            it.archiveFile.orNull?.asFile
        }.findLast {
            it.isFile
        }