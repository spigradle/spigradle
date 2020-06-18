package kr.entree.spigradle.module.bungee

import groovy.lang.Closure
import kr.entree.spigradle.data.BungeeDebug
import kr.entree.spigradle.internal.StandardDescription
import kr.entree.spigradle.module.common.debugDir
import org.gradle.api.Project
import java.io.File

/**
 * Created by JunHyung Lim on 2020-05-22
 */
open class BungeeExtension(project: Project) : StandardDescription {
    override var main: String? = null
    override var name: String? = null
    override var version: String? = null
    var description: String? = null
    var author: String? = null
    var depends: List<String> = emptyList()
    var softDepends: List<String> = emptyList()

    @Transient
    val debug: BungeeDebug = BungeeDebug(
            File(project.debugDir, "bungee/bungee.jar")
    )

    fun debug(configure: Closure<*>) {
        configure.delegate = debug
        configure.call()
    }

    fun debug(configure: BungeeDebug.() -> Unit) = configure(debug)

    fun depends(vararg depends: String) {
        this.depends = depends.toList()
    }

    fun softDepends(vararg softDepends: String) {
        this.softDepends = softDepends.toList()
    }
}