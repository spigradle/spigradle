package kr.entree.spigradle.util.attr

import kr.entree.spigradle.util.Mapper


/**
 * Created by JunHyung Lim on 2019-12-13
 */
class Permission extends Mapper {
    final String name
    String description
    boolean defaults
    Map<String, Boolean> children

    Permission(String name) {
        this.name = name
    }

    @Override
    Class<?> thisClass() {
        return Permission
    }
}
