package kr.entree.spigradle.internal

import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.TaskContainer
import org.gradle.api.tasks.TaskProvider
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.gradle.plugins.ide.idea.model.IdeaProject
import org.jetbrains.gradle.ext.ProjectSettings
import org.jetbrains.gradle.ext.RunConfigurationContainer

internal inline fun <T> notNull(any: T?, message: () -> String = { "" }): T {
    return any ?: throw GradleException(message())
}

internal fun TaskContainer.findArtifactJar() =
        withType<Jar>().mapNotNull { // TODO: Check shadowJar
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

internal fun IdeaProject.settings(configure: ProjectSettings.() -> Unit = {}) =
        (this as ExtensionAware).extensions.getByType(ProjectSettings::class).apply(configure)

internal fun ProjectSettings.runConfigurations(configure: RunConfigurationContainer.() -> Unit) =
        ((this as ExtensionAware).extensions["runConfigurations"] as RunConfigurationContainer).apply(configure)