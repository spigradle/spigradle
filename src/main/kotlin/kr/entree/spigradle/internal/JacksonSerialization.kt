package kr.entree.spigradle.internal

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
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
    override fun serialize(
            value: GeneratedSubclass,
            gen: JsonGenerator,
            provider: SerializerProvider
    ) = provider.findValueSerializer(value.publicType()).serialize(value, gen, provider)
}