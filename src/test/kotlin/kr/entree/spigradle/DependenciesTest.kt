package kr.entree.spigradle

import kr.entree.spigradle.data.Dependencies
import kr.entree.spigradle.data.Dependency
import kr.entree.spigradle.data.SpigotDependencies
import kr.entree.spigradle.data.SpigotRepositories
import kr.entree.spigradle.internal.toFieldEntries
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

    @Test
    fun test() {
        SpigotRepositories.toFieldEntries<String>().forEach { (name, string) ->
            println(name)
            println(string)
        }
    }
}