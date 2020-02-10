package kr.entree.spigradle.util.attr

import kr.entree.spigradle.util.annotation.ActualName
import kr.entree.spigradle.util.annotation.MappingObject

/**
 * Created by JunHyung Lim on 2019-12-13
 */
@MappingObject
class Permission {
    final String name
    String description
    @ActualName('default')
    String defaults
    Map<String, Boolean> children = new HashMap<>()

    Permission(String name) {
        this.name = name
    }
}
