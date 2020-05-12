package kr.entree.spigradle.kotlin

import kr.entree.spigradle.data.Dependencies
import org.gradle.api.artifacts.dsl.DependencyHandler

/**
 * Created by JunHyung Lim on 2020-05-12
 */
fun DependencyHandler.spigradle(version: String? = null) = Dependencies.SPIGRADLE.format(version)

fun DependencyHandler.lombok(version: String? = null) = Dependencies.LOMBOK.format(version)