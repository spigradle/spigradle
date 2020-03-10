package kr.entree.spigradle.util

import kr.entree.spigradle.mapper.ActualNames

/**
 * Created by JunHyung Lim on 2020-02-09
 */
class Enums {
    static <T extends Enum<T>> T get(Class<T> type, String name) {
        def aliases = type.fields.findAll {
            it.isEnumConstant()
        }.collectEntries { [ActualNames.get(it), it.name] } as Map<String, String>
        try {
            def mappedName = aliases.get(name) ?: name
            return type.valueOf(mappedName)
        } catch (IllegalArgumentException ex) {
            def values = type.values() as List<T>
            def availables = aliases.keySet() + values.collect { it.name() }
            throw new IllegalArgumentException("Available '${type.simpleName}' enum names: [${availables.join(', ')}]", ex)
        }
    }
}
