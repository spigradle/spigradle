package kr.entree.spigradle.bungee

import kr.entree.spigradle.module.bungee.BungeePlugin
import kr.entree.spigradle.util.testGradleScript
import org.junit.jupiter.api.Test

/**
 * Created by JunHyung Lim on 2020-05-23
 */
class BungeeGradleTest {
    @Test
    fun kotlin() {
        testGradleScript("/bungee/kotlin", BungeePlugin.DESC_GEN_TASK_NAME)
    }

    @Test
    fun groovy() {
        testGradleScript("/bungee/groovy", BungeePlugin.DESC_GEN_TASK_NAME)
    }
}