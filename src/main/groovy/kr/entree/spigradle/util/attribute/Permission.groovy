package kr.entree.spigradle.util.attribute

import kr.entree.spigradle.util.annotation.RenameTo
import kr.entree.spigradle.util.annotation.MappingObject

/**
 * Created by JunHyung Lim on 2019-12-13
 */
@MappingObject
class Permission {
    final transient String name
    String description
    @RenameTo('default')
    String defaults
    Map<String, Boolean> children = new HashMap<>()

    Permission(String name) {
        this.name = name
    }
}
