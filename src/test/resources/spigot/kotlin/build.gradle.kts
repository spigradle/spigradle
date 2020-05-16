import kr.entree.spigradle.kotlin.*

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