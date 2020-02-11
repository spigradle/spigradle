package kr.entree.spigradle.util.attr

import kr.entree.spigradle.util.annotation.RenameTo
import kr.entree.spigradle.util.annotation.MappingObject

/**
 * Created by JunHyung Lim on 2019-12-13
 */
@MappingObject
class Command {
    final transient String name
    String description
    String usage
    String permission
    @RenameTo('permission-message')
    String permissionMessage
    List<String> aliases = new ArrayList<>()

    Command(String name) {
        this.name = name
    }
}
