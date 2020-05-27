package kr.entree.spigradle.module.bungee

import kr.entree.spigradle.internal.StandardDescription
import org.gradle.api.Project

/**
 * Created by JunHyung Lim on 2020-05-22
 */
open class BungeeDescription(project: Project) : StandardDescription {
    override var main: String? = null
    override var name: String? = null
    override var version: String? = null
    var description: String? = null
    var author: String? = null
    var depends: List<String> = emptyList()
    var softDepends: List<String> = emptyList()
}