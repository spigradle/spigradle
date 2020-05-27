package kr.entree.spigradle.spigot

import com.fasterxml.jackson.databind.ObjectMapper
import kr.entree.spigradle.module.common.Download
import kr.entree.spigradle.module.spigot.BuildSpigot
import kr.entree.spigradle.module.spigot.RunSpigot
import org.gradle.api.file.CopySpec
import org.gradle.kotlin.dsl.closureOf
import org.gradle.kotlin.dsl.creating
import org.gradle.kotlin.dsl.getValue
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.io.TempDir
import java.io.File

/**
 * Created by JunHyung Lim on 2020-05-26
 */
class PrepareServerTest {
    //    @Test TODO: Need to think how it test
    fun prepare(@TempDir tempFile: File) {
        // downloadBuildTools -> buildSpigot -> copySpigot -> runSpigot -> copyPlugins(TO-DO)
        val project = ProjectBuilder.builder().build()
        val buildToolJar = File(tempFile, "tools/BuildTools.jar").apply { parentFile.mkdirs() }
        val serverJar = File(buildToolJar.parentFile, "server/spigot.jar").apply { parentFile.mkdirs() }
        val downloadBuildTools by project.tasks.creating(Download::class) {
            source = "https://hub.spigotmc.org/jenkins/job/BuildTools/lastSuccessfulBuild/artifact/target/BuildTools.jar"
            destination = buildToolJar
        }
        val buildSpigot by project.tasks.creating(BuildSpigot::class) {
            this.buildToolJar = downloadBuildTools.destination
            version = "1.15.2"
        }
        downloadBuildTools.download()
        if (!serverJar.isFile) {
            buildSpigot.exec()
        }
        // https://hub.spigotmc.org/stash/projects/SPIGOT/repos/buildtools/browse/src/main/java/org/spigotmc/builder/Builder.java#641
        val buildJsonObject = ObjectMapper().readTree(File(buildToolJar.parentFile, "BuildData/info.json"))
        val versionName = buildJsonObject["minecraftVersion"].asText()
        val fileName = "spigot-$versionName.jar"
        val buildOutputJar = File(buildSpigot.outputDirectory ?: buildToolJar.parentFile, fileName)
        project.copy(closureOf<CopySpec> {
            from(buildOutputJar)
            into(serverJar.parentFile)
            rename { serverJar.name }
        })
        project.tasks.creating(RunSpigot::class) {
            eula = true
            spigotJar = serverJar
            args("nogui")
            exec()
        }
    }
}