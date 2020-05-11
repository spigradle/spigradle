package kr.entree.spigradle.spigot.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * Created by JunHyung Lim on 2020-05-03
 */
@Serializable
class Permission {
    constructor(name: String) {
        this.name = name
    }

    @Transient
    var name: String = ""
    var description: String? = null
    var defaults: String? = null
    val children = mutableMapOf<String, Boolean>()
}