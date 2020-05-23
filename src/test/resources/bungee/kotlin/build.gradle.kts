import kr.entree.spigradle.kotlin.bungeecord

plugins {
    id("kr.entree.spigradle.bungee")
    kotlin("jvm") version "1.3.72"
}

repositories {
    mavenLocal()
}

dependencies {
    compileOnly(bungeecord())
}

bungee {
    author = "EntryPoint"
}