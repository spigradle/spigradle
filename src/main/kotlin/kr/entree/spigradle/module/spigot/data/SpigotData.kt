package kr.entree.spigradle.module.spigot.data

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import kr.entree.spigradle.internal.SerialName
import kr.entree.spigradle.internal.Transient
import org.gradle.api.plugins.ExtraPropertiesExtension

/**
 * Created by JunHyung Lim on 2020-05-11
 */
fun ExtraPropertiesExtension.setLoadGroovyExtension() {
    set("POST_WORLD", Load.POST_WORLD)
    set("STARTUP", Load.STARTUP)
}

enum class Load {
    @SerialName("POSTWORLD")
    POST_WORLD,
    STARTUP
}

@JsonPropertyOrder("description", "usage", "permission", "permission-message")
open class Command(@Transient val name: String) {
    var description: String? = null
    var usage: String? = null
    var permission: String? = null

    @SerialName("permission-message")
    var permissionMessage: String? = null
    var aliases = emptyList<String>()
}

@JsonPropertyOrder("description", "default", "children")
open class Permission(@Transient val name: String) {
    var description: String? = null

    @SerialName("default")
    var defaults: String? = null
    var children = emptyMap<String, Boolean>()
}