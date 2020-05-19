@file:Suppress("UNUSED_PARAMETER")

package kr.entree.spigradle.internal

import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Plugin
import org.gradle.api.Task
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.plugins.Convention
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.PluginManager
import org.gradle.api.tasks.TaskCollection
import org.gradle.api.tasks.TaskContainer

/**
 * Created by JunHyung Lim on 2020-04-28
 */
internal inline fun <reified T> ExtensionContainer.create(name: String, vararg parameters: Any, configure: T.() -> Unit = {}) = create(name, T::class.java, *parameters)

internal inline fun <reified T> ExtensionContainer.findByBoth(name: String, configure: T.() -> Unit = {}) = (findByName(name) as? T)?.apply(configure)

internal inline fun <reified T : Task> TaskContainer.create(name: String, vararg parameters: Any, configure: T.() -> Unit = {}): T = create(name, T::class.java, *parameters).apply(configure)

internal inline fun <reified T : Task> TaskContainer.withType(configure: T.() -> Unit = {}): TaskCollection<T> {
    return withType(T::class.java).apply { forEach(configure) }
}

internal inline fun <reified T : Task> TaskContainer.findByBoth(name: String, configure: T.() -> Unit = {}) = (findByName(name) as? T)?.apply(configure)

internal inline fun <reified T : Plugin<out Any>> PluginManager.apply() = apply(T::class.java)

internal fun DependencyHandler.compileOnly(notation: Any) = add(JavaPlugin.COMPILE_ONLY_CONFIGURATION_NAME, notation)

internal fun DependencyHandler.annotationProcessor(notation: Any) = add(JavaPlugin.ANNOTATION_PROCESSOR_CONFIGURATION_NAME, notation)

internal fun DependencyHandler.kapt(notation: Any) = add("kapt", notation)

internal inline fun <reified T> Convention.getPlugin() = getPlugin(T::class.java)

internal operator fun <T> NamedDomainObjectContainer<T>.get(name: String) = getByName(name)