/*
 * Copyright (c) 2025 Spigradle contributors.
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

package kr.entree.spigradle

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.internal.GeneratedSubclass

class NamedDomainObjectContainerSerializer : StdSerializer<NamedDomainObjectContainer<*>>(NamedDomainObjectContainer::class.java) {
    override fun serialize(
            value: NamedDomainObjectContainer<*>,
            gen: JsonGenerator,
            provider: SerializerProvider
    ) = provider.defaultSerializeValue(value.asMap, gen)

    override fun isEmpty(
            provider: SerializerProvider,
            value: NamedDomainObjectContainer<*>
    ) = value.isEmpty()
}

class GeneratedSubclassSerializer : StdSerializer<GeneratedSubclass>(GeneratedSubclass::class.java) {
    private fun GeneratedSubclass.findPublicType() = Class.forName(javaClass.name.substringBefore("_Decorated"))

    override fun serialize(
            value: GeneratedSubclass,
            gen: JsonGenerator,
            provider: SerializerProvider
    ) {
        provider.findValueSerializer(value.findPublicType()).serialize(value, gen, provider)
    }

    override fun isEmpty(
            provider: SerializerProvider,
            value: GeneratedSubclass
    ) = provider.findValueSerializer(value.findPublicType()).isEmpty(provider, value)
}

object Jackson {
    val GRADLE_MODULE = SimpleModule()
            .addSerializer(NamedDomainObjectContainerSerializer())
            .addSerializer(GeneratedSubclassSerializer())
    val JSON = ObjectMapper()
            .registerKotlinModule()
            .registerModule(GRADLE_MODULE)
            .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
    val YAML = ObjectMapper(YAMLFactory())
            .registerKotlinModule()
            .registerModule(GRADLE_MODULE)
            .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
}

// It breaks property order, related: com.fasterxml.jackson.databind.introspect.POJOPropertiesCollector#_renameProperties()
internal typealias SerialName = JsonProperty

internal typealias Transient = JsonIgnore

internal typealias Serialize = JsonSerialize