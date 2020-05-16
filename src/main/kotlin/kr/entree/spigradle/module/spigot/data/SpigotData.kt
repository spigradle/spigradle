package kr.entree.spigradle.module.spigot.data

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import kr.entree.spigradle.internal.SerialName
import kr.entree.spigradle.internal.Transient

/**
 * Created by JunHyung Lim on 2020-05-11
 */
enum class Load {
    @SerialName("POSTWORLD")
    POST_WORLD,
    STARTUP
}

@JsonPropertyOrder("description", "usage", "permission", "permission-message")
class Command(@Transient val name: String) {
    var description: String? = null
    var usage: String? = null
    var permission: String? = null
    @SerialName("permission-message")
    var permissionMessage: String? = null
    var aliases = emptyList<String>()
}

@JsonPropertyOrder("description", "default", "children")
class Permission(@Transient val name: String) {
    var description: String? = null
    @SerialName("default")
    var defaults: String? = null
    var children = emptyMap<String, Boolean>()
}