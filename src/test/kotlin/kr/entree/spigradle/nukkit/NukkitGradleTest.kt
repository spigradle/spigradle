package kr.entree.spigradle.nukkit

import kr.entree.spigradle.module.nukkit.NukkitPlugin
import kr.entree.spigradle.util.testGradleScriptWithResource
import org.junit.jupiter.api.Test

/**
 * Created by JunHyung Lim on 2020-05-23
 */
class NukkitGradleTest {
    @Test
    fun kotlin() {
        testGradleScriptWithResource("/nukkit/kotlin", NukkitPlugin.DESC_GEN_TASK_NAME)
    }

    @Test
    fun groovy() {
        testGradleScriptWithResource("/nukkit/groovy", NukkitPlugin.DESC_GEN_TASK_NAME)
    }
}