package kr.entree.spigradle.internal

import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.TaskContainer
import org.gradle.api.tasks.TaskProvider
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

internal fun lazyString(provider: () -> Any): Any = object {
    override fun toString(): String {
        return provider().toString()
    }
}

internal fun <T : Task> TaskProvider<T>.applyToConfigure(configure: T.() -> Unit): TaskProvider<T> {
    return apply { configure(configure) }
}

internal inline fun <T> Project.cachingProvider(crossinline provider: () -> T): Provider<T> {
    var cache: T? = null
    return provider {
        if (cache == null) {
            cache = provider()
        }
        cache
    }
}