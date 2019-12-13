package kr.entree.spigradle.extension


import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.artifacts.repositories.ArtifactRepository

/**
 * Created by JunHyung Lim on 2019-12-13
 */
class SpigotRepositories {
    final RepositoryHandler handler

    SpigotRepositories(RepositoryHandler handler) {
        this.handler = handler
    }

    ArtifactRepository spigot() {
        return handler.maven {
            url 'https://hub.spigotmc.org/nexus/content/repositories/snapshots/'
        }
    }
}
