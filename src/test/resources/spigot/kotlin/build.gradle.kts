import kr.entree.spigradle.kotlin.spigot

plugins {
    kotlin("jvm") version "1.3.72"
    id("kr.entree.spigradle")
}

repositories {
    mavenLocal()
}

dependencies {
    compileOnly(spigot())
}

spigot {
    main = "ABC"
}

tasks {
    build.get().doLast {
        val pluginFile = File(generateSpigotDescription.get().temporaryDir, "plugin.yml")
        if (pluginFile.isFile) {
            println(pluginFile.absolutePath)
            println(pluginFile.bufferedReader().readText())
        } else {
            throw GradleException("Error!")
        }
    }
}