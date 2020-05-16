package kr.entree.spigradle

import kr.entree.spigradle.data.Dependencies
import kr.entree.spigradle.data.Dependency
import kr.entree.spigradle.internal.toFieldEntries
import kr.entree.spigradle.module.spigot.data.SpigotDependencies
import kotlin.test.Test

/**
 * Created by JunHyung Lim on 2020-05-12
 */
class DependenciesTest {
    @Test
    fun `validate dependencies`() {
        listOf(SpigotDependencies, Dependencies).flatMap { depObj ->
            depObj.toFieldEntries<Dependency> { "${depObj.javaClass.simpleName}.$it" }
        }.forEach {
            println(it)
        }
    }
}