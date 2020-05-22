package kr.entree.spigradle.kotlin

import kr.entree.spigradle.data.Dependencies
import kr.entree.spigradle.data.Repositories
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.kotlin.dsl.maven

/**
 * Repositories
 */
fun RepositoryHandler.sonatype(configure: MavenArtifactRepository.() -> Unit = {}) = maven(Repositories.SONATYPE, configure)

fun RepositoryHandler.jitpack(configure: MavenArtifactRepository.() -> Unit = {}) = maven(Repositories.JITPACK, configure)

/**
 * Dependencies
 */
fun DependencyHandler.spigradle(version: String? = null) = Dependencies.SPIGRADLE.format(version)

fun DependencyHandler.lombok(version: String? = null) = Dependencies.LOMBOK.format(version)