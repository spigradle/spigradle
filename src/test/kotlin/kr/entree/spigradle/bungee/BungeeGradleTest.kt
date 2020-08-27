package kr.entree.spigradle.bungee

import kr.entree.spigradle.module.bungee.BungeePlugin
import kr.entree.spigradle.util.testGradleScriptWithResource
import org.junit.jupiter.api.Test

/**
 * Created by JunHyung Lim on 2020-05-23
 */
class BungeeGradleTest {
    @Test
    fun kotlin() {
        testGradleScriptWithResource("/bungee/kotlin", BungeePlugin.BUNGEE_TYPE.descGenTask)
    }

    @Test
    fun groovy() {
        testGradleScriptWithResource("/bungee/groovy", BungeePlugin.BUNGEE_TYPE.descGenTask)
    }
}