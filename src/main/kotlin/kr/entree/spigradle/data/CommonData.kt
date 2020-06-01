package kr.entree.spigradle.data

import kr.entree.spigradle.SpigradleMeta
import java.io.File

/**
 * Created by JunHyung Lim on 2020-05-22
 */
data class FileEntry(var file: File, var directory: File = file.parentFile)

object Dependencies {
    val LOMBOK = Dependency("org.projectlombok", "lombok", "1.18.12")
    val SPIGRADLE = Dependency("kr.entree", "spigradle", SpigradleMeta.VERSION)
}

object Repositories {
    val SONATYPE = "https://oss.sonatype.org/content/repositories/snapshots/"
    val JITPACK = "https://jitpack.io"
}

data class Dependency(
        val group: String,
        val name: String,
        val version: String,
        val versionModifier: (String) -> String = { it }
) {
    fun adjustVersion(inputVersion: String?) = inputVersion?.run(versionModifier) ?: version

    fun format(inputVersion: String? = null): String {
        return "$group:$name:${adjustVersion(inputVersion)}"
    }
}

inline fun Dependency(
        dependency: Dependency,
        group: String = dependency.group,
        name: String = dependency.name,
        version: String = dependency.version,
        noinline versionModifier: (String) -> String = dependency.versionModifier,
        configure: Dependency.() -> Unit = {}
) = Dependency(group, name, version, versionModifier).apply(configure)

object VersionModifier {
    val SNAPSHOT_APPENDER = createAdjuster("SNAPSHOT")
    val SPIGOT_ADJUSTER = createAdjuster("R0.1", "SNAPSHOT")

    fun createAdjuster(vararg tags: String): (String) -> String = { version ->
        buildString {
            val pieces = version.split("-")
            append(version)
            tags.drop(pieces.size - 1).forEach {
                append("-").append(it)
            }
        }
    }
}