import kr.entree.spigradle.kotlin.nukkit

plugins {
    kotlin("jvm") version "1.4.20"
    id("kr.entree.spigradle.nukkit")
}

repositories {
    mavenLocal()
}

dependencies {
    compileOnly(nukkit())
}

tasks {
    detectNukkitMain.get().enabled = false
}

nukkit {
    authors = listOf("EntryPoint")
}