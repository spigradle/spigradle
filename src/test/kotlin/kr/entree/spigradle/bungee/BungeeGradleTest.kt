package kr.entree.spigradle.bungee

import kr.entree.spigradle.module.bungee.BungeePlugin
import kr.entree.spigradle.util.testGradleTaskWithResource
import org.junit.jupiter.api.Test
import kotlin.test.Ignore

@Ignore
class BungeeGradleTest {
    @Test
    fun kotlin() {
        testGradleTaskWithResource("/bungee/kotlin", BungeePlugin.BUNGEE_TYPE.descGenTask)
    }

    @Test
    fun groovy() {
        // NOTE: This will crash if versioning up Kotlin! maybe conflicts Kotlin 1.5+ on JVM 8.
        testGradleTaskWithResource("/bungee/groovy", BungeePlugin.BUNGEE_TYPE.descGenTask)
    }
}