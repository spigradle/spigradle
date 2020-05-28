package kr.entree.spigradle.module.common

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.SkipWhenEmpty
import org.gradle.api.tasks.TaskAction
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

    @get:Input
    @SkipWhenEmpty
    lateinit var source: String

    @get:OutputFile
    @SkipWhenEmpty
    lateinit var destination: File

    @TaskAction
    fun download() = destination.apply {
        parentFile.mkdirs()
    }.writeBytes(URL(source).readBytes())
}