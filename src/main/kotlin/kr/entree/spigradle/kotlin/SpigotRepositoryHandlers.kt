package kr.entree.spigradle.kotlin

import kr.entree.spigradle.module.spigot.data.SpigotRepositories
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

fun RepositoryHandler.spigotmc(configure: MavenArtifactRepository.() -> Unit = {}) = maven(SpigotRepositories.SPIGOT_MC, configure)

fun RepositoryHandler.sonatype(configure: MavenArtifactRepository.() -> Unit = {}) = maven(SpigotRepositories.SONATYPE, configure)

fun RepositoryHandler.bungeecord(configure: MavenArtifactRepository.() -> Unit = {}) = sonatype(configure)

fun RepositoryHandler.papermc(configure: MavenArtifactRepository.() -> Unit = {}) = maven(SpigotRepositories.PAPER_MC, configure)

fun RepositoryHandler.protocolLib(configure: MavenArtifactRepository.() -> Unit = {}) = maven(SpigotRepositories.PROTOCOL_LIB, configure)

fun RepositoryHandler.jitpack(configure: MavenArtifactRepository.() -> Unit = {}) = maven(SpigotRepositories.JITPACK, configure)

fun RepositoryHandler.vault(configure: MavenArtifactRepository.() -> Unit = {}) = jitpack(configure)

fun RepositoryHandler.enginehub(configure: MavenArtifactRepository.() -> Unit = {}) = maven(SpigotRepositories.ENGINE_HUB, configure)

fun RepositoryHandler.codemc(configure: MavenArtifactRepository.() -> Unit = {}) = maven(SpigotRepositories.CODE_MC, configure)

fun RepositoryHandler.enderZone(configure: MavenArtifactRepository.() -> Unit = {}) = maven(SpigotRepositories.ENDER_ZONE, configure)

fun RepositoryHandler.essentialsX(configure: MavenArtifactRepository.() -> Unit = {}) = enderZone(configure)

fun RepositoryHandler.frostcast(configure: MavenArtifactRepository.() -> Unit = {}) = maven(SpigotRepositories.FROSTCAST, configure)

fun RepositoryHandler.banManager(configure: MavenArtifactRepository.() -> Unit = {}) = frostcast(configure)

fun RepositoryHandler.bStats(configure: MavenArtifactRepository.() -> Unit = {}) = codemc(configure)