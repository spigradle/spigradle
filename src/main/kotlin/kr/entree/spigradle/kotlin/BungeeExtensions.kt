package kr.entree.spigradle.kotlin

import kr.entree.spigradle.data.BungeeDependencies
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.artifacts.repositories.MavenArtifactRepository

/**
 * Repositories
 */
fun RepositoryHandler.bungeecord(configure: MavenArtifactRepository.() -> Unit = {}) = sonatype(configure)

/**
 * Dependencies
 */
fun DependencyHandler.bungeecord(version: String? = null) = BungeeDependencies.BUNGEE_CORD.format(version)