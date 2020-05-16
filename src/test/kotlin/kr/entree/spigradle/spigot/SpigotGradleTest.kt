package kr.entree.spigradle.spigot

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import java.io.File
import kotlin.test.Test
import kotlin.test.assertNotEquals

/**
 * Created by JunHyung Lim on 2020-05-16
 */
class SpigotGradleTest {
    @Test
    fun kotlin() {
        val result = GradleRunner.create()
                .withProjectDir(File(javaClass.getResource("/spigot/kotlin").file))
                .withPluginClasspath()
                .withArguments("build", "--stacktrace")
                .build()
        assertNotEquals(TaskOutcome.FAILED, result.task(":spigotPluginYaml")!!.outcome)
    }

    @Test
    fun groovy() {

    }
}