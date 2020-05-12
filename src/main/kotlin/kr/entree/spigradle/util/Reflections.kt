package kr.entree.spigradle.util

import com.google.common.base.CaseFormat
import kotlinx.serialization.SerialName
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties

/**
 * Created by JunHyung Lim on 2020-05-11
 */
@OptIn(ExperimentalStdlibApi::class)
inline fun <reified V> Any.toFieldEntries(
        keyMapper: (String) -> String = { CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, it) }
): List<Pair<String, V>> {
    return this::class.memberProperties.map { property ->
        val renameAnnotation = property.findAnnotation<SerialName>()
        val name = renameAnnotation?.value ?: property.name
        keyMapper(name) to property.call(this) as V
    }
}