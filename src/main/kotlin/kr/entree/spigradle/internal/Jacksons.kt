package kr.entree.spigradle.internal

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.internal.GeneratedSubclass

/**
 * Created by JunHyung Lim on 2020-05-16
 */
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
    val GRADLE_MODULE: SimpleModule = SimpleModule()
            .addSerializer(NamedDomainObjectContainerSerializer())
            .addSerializer(GeneratedSubclassSerializer())
    val MAPPER: ObjectMapper = ObjectMapper()
            .registerModules(KotlinModule(), GRADLE_MODULE)
            .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
}

// It breaks property order, related: com.fasterxml.jackson.databind.introspect.POJOPropertiesCollector#_renameProperties()
internal typealias SerialName = JsonProperty

internal typealias Transient = JsonIgnore

internal typealias Serialize = JsonSerialize