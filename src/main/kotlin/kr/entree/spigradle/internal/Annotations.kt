package kr.entree.spigradle.internal

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonSerialize

/**
 * Created by JunHyung Lim on 2020-05-16
 */
// It breaks property order, related: com.fasterxml.jackson.databind.introspect.POJOPropertiesCollector#_renameProperties()
internal typealias SerialName = JsonProperty

internal typealias Transient = JsonIgnore

internal typealias Serialize = JsonSerialize