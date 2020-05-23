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

    fun setDefaults(project: Project) {
        name = name ?: project.name
        version = version ?: project.version.toString()
    }
}

abstract class DefaultDescription(project: Project) : StandardDescription {
    init {
        project.afterEvaluate {
            setDefaults(project)
        }
    }
}