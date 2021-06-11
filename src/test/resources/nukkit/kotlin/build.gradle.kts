import kr.entree.spigradle.kotlin.nukkit

plugins {
    kotlin("jvm") version "1.3.72"
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