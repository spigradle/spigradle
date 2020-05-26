package kr.entree.spigradle.module.spigot

import org.gradle.api.GradleException
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.SkipWhenEmpty
import java.io.File

/**
 * Created by JunHyung Lim on 2020-05-26
 */
open class BuildSpigotTask : JavaExec() { // Is it good to extends JavaExec? Another option is composition
    @get:InputFile
    var outputDirectory: File? = null

    @get:InputFile
    @SkipWhenEmpty
    var toolFile: File? = null
        set(value) {
            if (value != null) {
                classpath = project.files(value)
                workingDir = value.parentFile
            }
            field = value
        }

    @get:Input
    var version: String = "latest"

    override fun exec() {
        if (args.any { it == "--output-dir" }) {
            throw GradleException("Use the 'outputDirectory' property instead of args(\"--output-dir\")")
        }
        args("--rev", version)
        outputDirectory?.apply {
            args("--output-dir", absolutePath)
        }
        super.exec()
    }
}

open class RunSpigotTask : JavaExec() {
    @get:InputFile
    @SkipWhenEmpty
    var spigotFile: File? = null
        set(value) {
            if (value != null) {
                classpath = project.files(value)
                workingDir = value.parentFile
            }
            field = value
        }
    var eula: Boolean = false

    override fun exec() {
        if (!eula) {
            throw GradleException("""
                Please set the 'eula' property to true if you agree the Mojang EULA.
                https://account.mojang.com/documents/minecraft_eula
            """.trimIndent())
        }
        acceptEULA()
        super.exec()
    }

    private fun acceptEULA() {
        File(spigotFile!!.parentFile, "eula.txt").writeText("eula=true")
    }
}