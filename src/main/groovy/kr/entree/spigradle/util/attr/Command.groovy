package kr.entree.spigradle.util.attr

import kr.entree.spigradle.util.annotation.ActualName
import kr.entree.spigradle.util.annotation.MappingObject

/**
 * Created by JunHyung Lim on 2019-12-13
 */
@MappingObject
class Command {
    final String name
    String[] aliases
    String description
    String usage
    String permission
    @ActualName('permission-message')
    String permissionMessage

    Command(String name) {
        this.name = name
    }
}
