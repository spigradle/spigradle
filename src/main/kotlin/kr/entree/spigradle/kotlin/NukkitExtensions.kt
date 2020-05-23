package kr.entree.spigradle.kotlin

import kr.entree.spigradle.data.NukkitDependencies
import kr.entree.spigradle.data.NukkitRepositories
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.kotlin.dsl.maven

/**
 * Repositories
 */
fun RepositoryHandler.nukkitX(configure: MavenArtifactRepository.() -> Unit = {}) = maven(NukkitRepositories.NUKKIT_X, configure)

/**
 * Dependencies
 */
fun DependencyHandler.nukkit(version: String? = null) = NukkitDependencies.NUKKIT.format(version)