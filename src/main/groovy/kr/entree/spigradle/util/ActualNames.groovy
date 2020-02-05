package kr.entree.spigradle.util

import java.lang.reflect.Field

/**
 * Created by JunHyung Lim on 2020-02-05
 */
class ActualNames {
    static String get(Field field) {
        def actualName = field.getAnnotation(ActualName.class)
        return actualName != null ? actualName.value() : field.name
    }
}
