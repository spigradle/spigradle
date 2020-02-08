package kr.entree.spigradle

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Created by JunHyung Lim on 2019-12-11
 */
class SpigradlePlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        new SpigradleProject(project).with {
            setupRepositories()
            setupDependencies()
            setupPlugin()
        }
    }
}