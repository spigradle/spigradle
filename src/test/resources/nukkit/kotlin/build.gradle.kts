import kr.entree.spigradle.kotlin.nukkit

plugins {
    id("kr.entree.spigradle.nukkit")
    kotlin("jvm") version "1.3.72"
}

repositories {
    mavenLocal()
}

dependencies {
    compileOnly(nukkit())
}

nukkit {
    authors = listOf("EntryPoint")
}