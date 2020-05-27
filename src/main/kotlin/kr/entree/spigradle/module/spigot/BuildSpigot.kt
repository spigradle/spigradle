package kr.entree.spigradle.module.spigot

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.gradle.api.GradleException
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.JavaExec
import java.io.File

/**
 * Created by JunHyung Lim on 2020-05-26
 */
open class BuildSpigot : JavaExec() { // Is it good to extends JavaExec? Another option is composition
    companion object {
        private val mapper = ObjectMapper()
    }

    init {
        group = "spigradle"
        description = "Build the spigot.jar using BuildTools."
    }

    @get:Internal // TODO: Input?
    var buildToolJar: File? = null
        set(value) {
            if (value != null) {
                classpath = project.files(value)
                workingDir = value.parentFile
            }
            field = value
        }

    @get:Internal
    var outputDirectory: File? = null
        get() = field ?: buildToolJar?.parentFile

    @get:Internal
    var version: String = "latest"

    val buildData: Map<String, Any>
        get() {
            return buildToolJar?.run {
                val infoJson = File(parentFile, "BuildData/info.json")
                runCatching { mapper.readValue<Map<String, Any>>(infoJson) }.getOrNull()
            } ?: emptyMap()
        }

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