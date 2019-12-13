package kr.entree.spigradle.util

import java.lang.reflect.Modifier

/**
 * Created by JunHyung Lim on 2019-12-13
 */
abstract class Mapper {
    abstract Class<?> thisClass()

    Map<String, Object> map() {
        def map = [:] as Map<String, Object>
        thisClass().declaredFields.findAll {
            !it.synthetic && !Modifier.isFinal(it.modifiers)
        }.each {
            it.setAccessible(true)
            def value = it.get(this)
            if (value != null) {
                map[it.name] = Mapper.map(value)
            }
        }
        return map
    }

    static Object map(Object value) {
        if (value in Mapper) {
            return value.map()
        } else if (value instanceof Map) {
            value.entrySet().each {
                value[it.key] = map(it.value)
            }
        } else if (value instanceof List) {
            value.eachWithIndex { val, i ->
                value[i] = map(val)
            }
        } else if (value instanceof Collection) {
            def newCollection = []
            value.each {
                newCollection.add(map(it))
            }
            return newCollection
        }
        return value
    }
}
