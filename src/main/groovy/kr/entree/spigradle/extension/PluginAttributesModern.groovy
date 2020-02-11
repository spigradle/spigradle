package kr.entree.spigradle.extension

import kr.entree.spigradle.util.annotation.MappingObject
import kr.entree.spigradle.util.attr.Command
import kr.entree.spigradle.util.attr.Permission
import org.gradle.api.Project

import javax.inject.Inject

/**
 * Created by JunHyung Lim on 2020-02-11
 */
@MappingObject(PluginAttributes)
class PluginAttributesModern extends PluginAttributes {
    @Inject
    PluginAttributesModern(Project project) {
        super(project.container(Command), project.container(Permission))
    }
}
