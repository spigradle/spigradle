import kr.entree.spigradle.kotlin.spigot

plugins {
    kotlin("jvm") version "1.4.20"
    id("kr.entree.spigradle")
}

repositories {
    mavenLocal()
}

dependencies {
    compileOnly(spigot())
}

tasks {
    detectSpigotMain.get().enabled = false
    build.get().doLast {
        val pluginFile = File(sourceSets["main"].output.resourcesDir, "plugin.yml")
        if (pluginFile.isFile) {
            println(pluginFile.absolutePath)
            println(pluginFile.bufferedReader().readText())
        } else {
            throw GradleException("Error!")
        }
    }
}