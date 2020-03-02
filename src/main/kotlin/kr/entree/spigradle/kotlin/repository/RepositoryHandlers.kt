package kr.entree.spigradle.kotlin.repository

import kr.entree.spigradle.project.Repositories
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.artifacts.repositories.MavenArtifactRepository

/**
 * Created by JunHyung Lim on 2020-02-28
 */
internal inline fun RepositoryHandler.maven(
        url: String,
        crossinline configure: MavenArtifactRepository.() -> Unit = {}
) = maven {
    it.setUrl(url)
    configure(it)
}

fun RepositoryHandler.spigot(configure: MavenArtifactRepository.() -> Unit = {}) = maven(Repositories.SPIGOT, configure)

fun RepositoryHandler.bungeecord(configure: MavenArtifactRepository.() -> Unit = {}) = maven(Repositories.BUNGEECORD, configure)

fun RepositoryHandler.paper(configure: MavenArtifactRepository.() -> Unit = {}) = maven(Repositories.PAPER, configure)

fun RepositoryHandler.protocolLib(configure: MavenArtifactRepository.() -> Unit = {}) = maven(Repositories.PROTOCOL_LIB, configure)

fun RepositoryHandler.jitpack(configure: MavenArtifactRepository.() -> Unit = {}) = maven(Repositories.JITPACK, configure)

fun RepositoryHandler.vault(configure: MavenArtifactRepository.() -> Unit = {}) = jitpack(configure)

fun RepositoryHandler.enginehub(configure: MavenArtifactRepository.() -> Unit = {}) = maven(Repositories.ENGINEHUB, configure)

fun RepositoryHandler.codemc(configure: MavenArtifactRepository.() -> Unit = {}) = maven(Repositories.CODE_MC, configure)

fun RepositoryHandler.bStats(configure: MavenArtifactRepository.() -> Unit = {}) = codemc(configure)