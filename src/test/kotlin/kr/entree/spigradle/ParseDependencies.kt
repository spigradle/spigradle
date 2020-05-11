package kr.entree.spigradle

import com.google.common.base.CaseFormat
import kr.entree.spigradle.data.Dependencies
import kr.entree.spigradle.data.Dependency
import kr.entree.spigradle.spigot.data.SpigotDependencies
import org.junit.Test
import kotlin.reflect.full.memberProperties

/**
 * Created by JunHyung Lim on 2020-05-07
 */
class ParseDependencies {
    @Test
    fun test() {
        listOf(Dependencies, SpigotDependencies).flatMap { value ->
            value::class.memberProperties.map { property ->
                CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, property.name) to property.call(value) as Dependency
            }
        }.forEach {
            println(it)
        }
    }
}