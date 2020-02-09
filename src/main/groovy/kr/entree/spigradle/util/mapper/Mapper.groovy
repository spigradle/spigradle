package kr.entree.spigradle.util.mapper

import kr.entree.spigradle.util.annotation.MappingObject

import java.lang.reflect.Modifier

/**
 * Created by JunHyung Lim on 2019-12-13
 */
class Mapper {
    static Object map(Object object) {
        def mappingObject = object.getClass().getAnnotation(MappingObject)
        if (mappingObject != null) {
            def map = new LinkedHashMap<String, Object>()
            object.getClass().declaredFields.findAll {
                !it.synthetic && !Modifier.isFinal(it.modifiers) && !Modifier.isStatic(it.modifiers)
            }.each {
                it.setAccessible(true)
                def value = it.get(object)
                if (value != null) {
                    map[ActualNames.get(it)] = Mapper.map(value)
                }
            }
            return map
        } else if (object instanceof Map) {
            object.entrySet().each {
                object[it.key] = map(it.value)
            }
        } else if (object instanceof Collection) {
            def list = new ArrayList()
            object.each {
                list.add(map(it))
            }
            return list
        } else if (object instanceof Enum) {
            return ActualNames.get(object)
        }
        return object
    }
}
