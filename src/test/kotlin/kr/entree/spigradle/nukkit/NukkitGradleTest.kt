package kr.entree.spigradle.nukkit

import kr.entree.spigradle.module.nukkit.NukkitPlugin
import kr.entree.spigradle.util.testGradleScript
import org.junit.jupiter.api.Test

/**
 * Created by JunHyung Lim on 2020-05-23
 */
class NukkitGradleTest {
    @Test
    fun kotlin() {
        testGradleScript("/nukkit/kotlin", NukkitPlugin.DESC_GEN_TASK_NAME)
    }

    @Test
    fun groovy() {
        testGradleScript("/nukkit/groovy", NukkitPlugin.DESC_GEN_TASK_NAME)
    }
}