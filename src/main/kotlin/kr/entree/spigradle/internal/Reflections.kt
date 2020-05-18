package kr.entree.spigradle.internal

import com.fasterxml.jackson.annotation.JsonProperty
import com.google.common.base.CaseFormat
import java.lang.reflect.Field
import java.lang.reflect.Modifier

/**
 * Created by JunHyung Lim on 2020-05-11
 */
inline fun <reified T : Annotation> Field.findAnnotation(): T? {
    return runCatching { getAnnotation(T::class.java) as T }.getOrNull()
}

internal inline fun <reified V> Any.toFieldEntries(
        keyMapper: (String) -> String = { CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, it) }
): List<Pair<String, V>> {
    return this::class.java.declaredFields.filter {
        V::class.java.isAssignableFrom(it.type)
    }.map { field ->
        field.isAccessible = true
        val renameAnnotation = field.findAnnotation<SerialName>()
        val name = renameAnnotation?.value ?: field.name
        val value = if (Modifier.isStatic(field.modifiers)) field.get(null) else field.get(this)
        keyMapper(name) to value as V
    }
}