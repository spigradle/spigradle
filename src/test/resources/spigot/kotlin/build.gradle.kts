import kr.entree.spigradle.kotlin.spigot

plugins {
    java
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
        val pluginFile = File(spigotPluginYaml.get().temporaryDir, "plugin.yml")
        if (pluginFile.isFile) {
            println("Hi!")
            project.logger.info(pluginFile.readText())
            println(pluginFile.bufferedReader().readText())
            println(pluginFile.absolutePath)
            println("End")
        } else {
            throw GradleException("Error!")
        }
    }
}