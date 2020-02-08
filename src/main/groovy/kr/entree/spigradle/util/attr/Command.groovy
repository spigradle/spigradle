package kr.entree.spigradle.util.attr

import kr.entree.spigradle.util.ActualName
import kr.entree.spigradle.util.Mapper


/**
 * Created by JunHyung Lim on 2019-12-13
 */
class Command extends Mapper {
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

    @Override
    Class<?> thisClass() {
        return Command
    }
}
