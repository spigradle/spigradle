package kr.entree.spigradle.module.spigot

import org.gradle.api.GradleException
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.SkipWhenEmpty
import java.io.File

open class RunSpigot : JavaExec() {
    @get:Internal
    var spigotJar: File? = null
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
        File(spigotJar!!.parentFile, "eula.txt").writeText("eula=true")
    }
}