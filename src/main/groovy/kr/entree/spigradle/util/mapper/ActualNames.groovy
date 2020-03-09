package kr.entree.spigradle.util.mapper

import kr.entree.spigradle.annotation.RenameTo

import java.lang.reflect.Field

/**
 * Created by JunHyung Lim on 2020-02-05
 */
class ActualNames {
    static String getOrDefault(Field field, Object defValue = null) {
        def actualName = field.getAnnotation(RenameTo)
        return actualName != null ? actualName.value() : defValue
    }

    static String get(Field field) {
        return getOrDefault(field, field.name)
    }

    static <T extends Enum<T>> String get(Enum<T> anEnum) {
        return get(anEnum.getClass().getField(anEnum.name()))
    }
}
