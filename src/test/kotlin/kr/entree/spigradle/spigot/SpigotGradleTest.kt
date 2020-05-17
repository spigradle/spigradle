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
        test("/spigot/kotlin")
    }

    @Test
    fun groovy() {
        test("/spigot/groovy")
    }

    private fun test(path: String) {
        val result = GradleRunner.create()
                .withProjectDir(File(javaClass.getResource(path).file))
                .withPluginClasspath()
                .withArguments("build", "--stacktrace")
                .build()
        println(result.output)
        assertNotEquals(TaskOutcome.FAILED, result.task(":spigotPluginYaml")!!.outcome)
    }
}