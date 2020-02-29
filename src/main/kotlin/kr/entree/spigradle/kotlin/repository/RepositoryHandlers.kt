package kr.entree.spigradle.kotlin.repository

import kr.entree.spigradle.project.Repositories
import org.gradle.api.artifacts.dsl.RepositoryHandler

/**
 * Created by JunHyung Lim on 2020-02-28
 */
internal fun RepositoryHandler.maven(url: String) = maven {
    it.setUrl(url)
}

fun RepositoryHandler.spigot() = maven(Repositories.SPIGOT)

fun RepositoryHandler.bungeecord() = maven(Repositories.BUNGEECORD)

fun RepositoryHandler.paper() = maven(Repositories.PAPER)

fun RepositoryHandler.protocolLib() = maven(Repositories.PROTOCOL_LIB)

fun RepositoryHandler.jitpack() = maven(Repositories.JITPACK)

fun RepositoryHandler.vault() = jitpack()

fun RepositoryHandler.enginehub() = maven(Repositories.ENGINEHUB)