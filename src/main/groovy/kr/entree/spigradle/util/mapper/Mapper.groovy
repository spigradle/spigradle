package kr.entree.spigradle.util.mapper

import kr.entree.spigradle.util.annotation.MappingObject
import org.gradle.api.NamedDomainObjectContainer

import java.lang.reflect.Field
import java.lang.reflect.Modifier

/**
 * Created by JunHyung Lim on 2019-12-13
 */
class Mapper {
    static Object mapping(Object object, Boolean ignoreEmpty = true) {
        def mappingObject = object.getClass().getAnnotation(MappingObject)
        if (mappingObject != null) {
            def type = getType(mappingObject) ?: object.getClass()
            def map = new LinkedHashMap<String, Object>()
            getFields(type).each {
                it.setAccessible(true)
                def value = mapping(it.get(object), ignoreEmpty)
                if (value != null) {
                    map[ActualNames.get(it)] = value
                }
            }
            return map
        } else if (object instanceof NamedDomainObjectContainer) {
            def map = object.getAsMap()
            return mapping(map, ignoreEmpty)
        } else if (object instanceof Map) {
            if (ignoreEmpty && object.isEmpty()) {
                return null
            }
            return object.collectEntries {
                [it.key, mapping(it.value, ignoreEmpty)]
            }.findAll { it.value != null }
        } else if (object instanceof Collection) {
            if (ignoreEmpty && object.isEmpty()) {
                return null
            }
            return object.collect {
                mapping(it, ignoreEmpty)
            }.findAll { it != null }
        } else if (object instanceof Enum) {
            return ActualNames.get(object)
        }
        return object
    }

    static Class<?> getType(MappingObject mappingObject) {
        def type = mappingObject.value()
        return type != Void ? type : null
    }

    static List<Field> getFields(Class<?> type) {
        def ret = []
        def parent = type
        while (parent != null && parent != Object) {
            ret += parent.declaredFields.findAll {
                !it.synthetic && !Modifier.isStatic(it.modifiers)
            }
            parent = parent.superclass
        }
        return ret
    }
}
