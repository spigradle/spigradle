package kr.entree.spigradle.data

import com.google.common.base.CaseFormat
import kr.entree.spigradle.SpigradleMeta
import kr.entree.spigradle.spigot.data.SpigotDependencies
import kotlin.reflect.full.memberProperties

val Dependencies.all by lazy {
    listOf(Dependencies, SpigotDependencies).flatMap { value ->
        value::class.memberProperties.map { property ->
            CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, property.name)!! to property.call(value) as Dependency
        }
    }
}

object Dependencies {
    val LOMBOK = Dependency("org.projectlombok", "lombok", "1.18.12")
    val SPIGRADLE = Dependency("kr.entree", "spigradle", SpigradleMeta.VERSION)
}