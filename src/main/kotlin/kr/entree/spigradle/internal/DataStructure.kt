package kr.entree.spigradle.internal

import org.gradle.api.Project

/**
 * Created by JunHyung Lim on 2020-05-18
 */
interface MainProvider {
    var main: String?
}

interface StandardDescription : MainProvider {
    var name: String?
    var version: String?

    fun init(project: Project) {
        name = name ?: project.name
        version = version ?: project.version.toString()
    }
}