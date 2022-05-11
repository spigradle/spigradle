package kr.entree.spigradle.spigot

import kr.entree.spigradle.module.spigot.SpigotPlugin
import kr.entree.spigradle.util.testGradleTaskWithResource
import kotlin.test.Test

class SpigotGradleTest {
    @Test
    fun kotlin() {
        testGradleTaskWithResource("/spigot/kotlin", SpigotPlugin.SPIGOT_TYPE.descGenTask)
    }

    @Test
    fun groovy() {
        testGradleTaskWithResource("/spigot/groovy", SpigotPlugin.SPIGOT_TYPE.descGenTask)
    }

    @Test
    fun shadowLibraries() {
        testGradleTaskWithResource("/spigot/shadowLibraries", SpigotPlugin.SPIGOT_TYPE.descGenTask)
    }
}