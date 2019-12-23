package kr.entree.spigradle


import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.util.GradleVersion

/**
 * Created by JunHyung Lim on 2019-12-11
 */
class SpigradlePlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        checkGradleVersion()
        new SpigradleProject(project).with {
            setupRepositories()
            setupDependencies()
            setupPlugin()
        }
    }

    static def checkGradleVersion() {
        if (GradleVersion.current() < GradleVersion.version('6.0')) {
            throw new RuntimeException("Spigradle only supports Gradle 6.0+. Check it: https://github.com/EntryPointKR/Spigradle#requirements")
        }
    }
}