package kr.entree.spigradle.internal

import org.gradle.api.plugins.ExtraPropertiesExtension

/**
 * Created by JunHyung Lim on 2020-05-16
 */
class Groovies {
    static ExtraPropertiesExtension getExtensionFrom(object) {
        return object.ext // For dynamic call
    }
}
