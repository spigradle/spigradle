package kr.entree.spigradle.spigot

import kr.entree.spigradle.module.spigot.SpigotPlugin
import kr.entree.spigradle.util.testGradleScriptWithResource
import kotlin.test.Test

/**
 * Created by JunHyung Lim on 2020-05-16
 */
class SpigotGradleTest {
    @Test
    fun kotlin() {
        testGradleScriptWithResource("/spigot/kotlin", SpigotPlugin.SPIGOT_TYPE.descGenTask)
    }

    @Test
    fun groovy() {
        testGradleScriptWithResource("/spigot/groovy", SpigotPlugin.SPIGOT_TYPE.descGenTask)
    }
}