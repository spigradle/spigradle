@file:Suppress("UnstableApiUsage")

package kr.entree.spigradle.module.common

import kr.entree.spigradle.SpigradleMeta
import org.gradle.api.DefaultTask
import org.gradle.api.internal.tasks.TaskExecutionOutcome
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.property
import java.io.File
import java.net.URL

/**
 * Created by JunHyung Lim on 2020-05-26
 */
open class Download : DefaultTask() {
    init {
        group = "spigradle"
        description = "Download the file from the given URL."
    }

    @Input
    val source: Property<String> = project.objects.property()

    @Input
    val skipWhenExists: Property<Boolean> = project.objects.property<Boolean>().convention(true)

    @OutputFile
    val destination: Property<File> = project.objects.property()

    @TaskAction
    fun download() {
        if (skipWhenExists.get() && destination.get().isFile) {
            state.setOutcome(TaskExecutionOutcome.SKIPPED)
        } else {
            downloadInternal()
        }
    }

    private fun downloadInternal() = destination.get().apply {
        parentFile?.mkdirs()
    }.writeBytes(URL(source.get()).openConnection().run {
        setRequestProperty("User-Agent", "Spigradle/${SpigradleMeta.VERSION}")
        getInputStream()
    }.readBytes())
}