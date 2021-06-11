import kr.entree.spigradle.kotlin.bungeecord

plugins {
    kotlin("jvm") version "1.3.72"
    id("kr.entree.spigradle.bungee")
}

repositories {
    mavenLocal()
}

dependencies {
    compileOnly(bungeecord())
}

tasks {
    detectBungeeMain.get().enabled = false
}

bungee {
    author = "EntryPoint"
}