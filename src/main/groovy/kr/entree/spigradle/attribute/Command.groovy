package kr.entree.spigradle.attribute

import kr.entree.spigradle.annotation.MappingObject
import kr.entree.spigradle.annotation.RenameTo

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
