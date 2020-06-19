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
 * Downloads a file from the given URL.
 *
 * Groovy Example:
 *
 * ```groovy
 * import kr.entree.spigradle.module.common.Download
 *
 * task downloadSomething(type: Download) {
 *   source = 'https://newurl.com'
 *   destination = file("$buildDir/download/something")
 *   skipWhenExists = true // Do not overwrite.
 * }
 * ```
 *
 * Kotlin Example:
 *
 * ```kotiln
 * import kr.entree.spigradle.module.common.Download
 *
 * tasks {
 *   val downloadSomething by registering(Download::class) {
 *     source.set("https://newurl.com")
 *     destination.set(file("$buildDir/download/something"))
 *     skipWhenExists.set(true)
 *   }
 * }
 * ```
 *
 * @since 1.3.0
 */
open class Download : DefaultTask() {
    init {
        group = "spigradle"
        description = "Download the file from the given URL."
    }

    /**
     * The URL for the download.
     */
    @Input
    val source: Property<String> = project.objects.property()

    /**
     * The option whether to skip overwrite when the destination file already exists.
     *
     * The download will be skipped by following cases about whether 'skipWhenExists':
     * 1. If true, skip when the file already exists.
     * 2. If false, skip when the task inputs changed, other word UP-TO-DATE check.
     *
     * Defaults to false.
     */
    @Input
    val skipWhenExists: Property<Boolean> = project.objects.property<Boolean>().convention(false)

    /**
     * The destination where will be downloaded.
     */
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