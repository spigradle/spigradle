package kr.entree.spigradle.bungee

import kr.entree.spigradle.module.bungee.BungeePlugin
import kr.entree.spigradle.util.testGradleScriptWithResource
import org.junit.jupiter.api.Test

class BungeeGradleTest {
    @Test
    fun kotlin() {
        testGradleScriptWithResource("/bungee/kotlin", BungeePlugin.BUNGEE_TYPE.descGenTask)
    }

    @Test
    fun groovy() {
        // NOTE: This will crash if versioning up Kotlin! maybe conflicts Kotlin 1.5+ on JVM 8.
        testGradleScriptWithResource("/bungee/groovy", BungeePlugin.BUNGEE_TYPE.descGenTask)
    }
}