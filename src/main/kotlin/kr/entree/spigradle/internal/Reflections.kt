/*
 * Copyright (c) 2020 Spigradle contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kr.entree.spigradle.internal

import com.google.common.base.CaseFormat
import java.lang.reflect.Field
import java.lang.reflect.Modifier

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
        val name = renameAnnotation?.value ?: keyMapper(field.name)
        val value = if (Modifier.isStatic(field.modifiers)) field.get(null) else field.get(this)
        name to value as V
    }
}