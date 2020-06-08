package kr.entree.spigradle.module.spigot

import org.gradle.api.GradleException
import org.gradle.api.Project
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Created by JunHyung Lim on 2020-06-06
 */
internal fun Project.ensureMinecraftEULA(directory: File, eula: Boolean) {
    File(directory, "eula.txt").takeIf { eulaFile ->
        !eulaFile.isFile && (eula || logger.run {
            quiet("""
                Are you agree the Mojang EULA? (https://account.mojang.com/documents/minecraft_eula)
                Your input (y)es or (n)o:
            """.trimIndent())
            readLine().equals("y", ignoreCase = true) || throw GradleException("""
                Please set the 'eula' property in spigot {} block to true if you agree the Mojang EULA.
                https://account.mojang.com/documents/minecraft_eula
            """.trimIndent()) // If input is not "y", throw up
        })
    }?.apply {
        writeText("""
            # Accepted by Spigradle
            # ${DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now())}
            eula=true
        """.trimIndent())
    }
}