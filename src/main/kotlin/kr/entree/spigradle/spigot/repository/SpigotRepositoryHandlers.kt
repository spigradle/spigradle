//package kr.entree.spigradle.repository
//
//import kr.entree.spigradle.project.Repositories
//import org.gradle.api.artifacts.dsl.RepositoryHandler
//import org.gradle.api.artifacts.repositories.MavenArtifactRepository
//
///**
// * Created by JunHyung Lim on 2020-02-28
// */
//internal inline fun RepositoryHandler.maven(
//        url: String,
//        crossinline configure: MavenArtifactRepository.() -> Unit = {}
//) = maven {
//    it.setUrl(url)
//    configure(it)
//}
//
//fun RepositoryHandler.spigot(configure: MavenArtifactRepository.() -> Unit = {}) = maven(kr.entree.spigradle.project.Repositories.SPIGOT, configure)
//
//fun RepositoryHandler.bungeecord(configure: MavenArtifactRepository.() -> Unit = {}) = maven(kr.entree.spigradle.project.Repositories.BUNGEECORD, configure)
//
//fun RepositoryHandler.paper(configure: MavenArtifactRepository.() -> Unit = {}) = maven(kr.entree.spigradle.project.Repositories.PAPER, configure)
//
//fun RepositoryHandler.protocolLib(configure: MavenArtifactRepository.() -> Unit = {}) = maven(kr.entree.spigradle.project.Repositories.PROTOCOL_LIB, configure)
//
//fun RepositoryHandler.jitpack(configure: MavenArtifactRepository.() -> Unit = {}) = maven(kr.entree.spigradle.project.Repositories.JITPACK, configure)
//
//fun RepositoryHandler.vault(configure: MavenArtifactRepository.() -> Unit = {}) = jitpack(configure)
//
//fun RepositoryHandler.enginehub(configure: MavenArtifactRepository.() -> Unit = {}) = maven(kr.entree.spigradle.project.Repositories.ENGINEHUB, configure)
//
//fun RepositoryHandler.codemc(configure: MavenArtifactRepository.() -> Unit = {}) = maven(kr.entree.spigradle.project.Repositories.CODE_MC, configure)
//
//fun RepositoryHandler.enderZone(configure: MavenArtifactRepository.() -> Unit = {}) = maven(kr.entree.spigradle.project.Repositories.ENDER_ZONE, configure)
//
//fun RepositoryHandler.essentialsX(configure: MavenArtifactRepository.() -> Unit = {}) = enderZone(configure)
//
//fun RepositoryHandler.frostcast(configure: MavenArtifactRepository.() -> Unit = {}) = maven(kr.entree.spigradle.project.Repositories.FROSTCAST, configure)
//
//fun RepositoryHandler.banManager(configure: MavenArtifactRepository.() -> Unit = {}) = frostcast(configure)
//
//fun RepositoryHandler.bStats(configure: MavenArtifactRepository.() -> Unit = {}) = codemc(configure)