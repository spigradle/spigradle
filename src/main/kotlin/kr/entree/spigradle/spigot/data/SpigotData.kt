package kr.entree.spigradle.spigot.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * Created by JunHyung Lim on 2020-05-11
 */
enum class Load {
    POST_WORLD,
    STARTUP
}

@Serializable
class Command {
    constructor(name: String) {
        this.name = name
    }

    @Transient
    var name: String = ""
    var description: String? = null
    var usage: String? = null
    var permission: String? = null
    var permissionMessage: String? = null
    val aliases = mutableListOf<String>()
}

@Serializable
class Permission {
    constructor(name: String) {
        this.name = name
    }

    @Transient
    var name: String = ""
    var description: String? = null
    @SerialName("default")
    var defaults: String? = null
    val children = mutableMapOf<String, Boolean>()
}