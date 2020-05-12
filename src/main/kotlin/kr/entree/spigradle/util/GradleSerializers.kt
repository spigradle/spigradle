@file:OptIn(InternalSerializationApi::class)

package kr.entree.spigradle.util

import kotlinx.serialization.*
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import org.gradle.api.NamedDomainObjectContainer

/**
 * Created by JunHyung Lim on 2020-05-04
 */
@Serializer(forClass = NamedDomainObjectContainer::class)
class NamedDomainObjectContainerSerializer<T>(
        elementSerializer: KSerializer<T>
) : KSerializer<NamedDomainObjectContainer<T>> {
    private val mapSerializer = MapSerializer(String.serializer(), elementSerializer)
    override val descriptor get() = mapSerializer.descriptor

    override fun serialize(encoder: Encoder, value: NamedDomainObjectContainer<T>) = mapSerializer.serialize(encoder, value.asMap)

    override fun deserialize(decoder: Decoder): NamedDomainObjectContainer<T> = throw UnsupportedOperationException()
}