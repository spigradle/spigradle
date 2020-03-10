package kr.entree.spigradle.attribute

import kr.entree.spigradle.annotation.MappingObject
import kr.entree.spigradle.annotation.RenameTo

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
