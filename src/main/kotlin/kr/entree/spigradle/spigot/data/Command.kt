package kr.entree.spigradle.spigot.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * Created by JunHyung Lim on 2020-05-03
 */
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