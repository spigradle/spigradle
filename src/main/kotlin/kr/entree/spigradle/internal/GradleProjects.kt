package kr.entree.spigradle.internal

import org.gradle.api.Plugin
import org.gradle.api.Task
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.plugins.PluginManager
import org.gradle.api.tasks.TaskCollection
import org.gradle.api.tasks.TaskContainer

/**
 * Created by JunHyung Lim on 2020-04-28
 */
internal inline fun <reified T> ExtensionContainer.create(name: String, vararg parameters: Any, configure: T.() -> Unit = {}) = create(name, T::class.java, *parameters)

internal inline fun <reified T> ExtensionContainer.findByBoth(name: String, configure: T.() -> Unit = {}) = (findByName(name) as? T)?.apply(configure)

internal inline fun <reified T : Task> TaskContainer.create(name: String, vararg parameters: Any, configure: T.() -> Unit = {}): T = create(name, T::class.java, *parameters)

internal inline fun <reified T : Task> TaskContainer.withType(configure: T.() -> Unit = {}): TaskCollection<T> {
    return withType(T::class.java)
}

internal inline fun <reified T : Task> TaskContainer.findByBoth(name: String, configure: T.() -> Unit) = (findByName(name) as? T)?.run(configure)

internal inline fun <reified T : Plugin<out Any>> PluginManager.apply() = apply(T::class.java)