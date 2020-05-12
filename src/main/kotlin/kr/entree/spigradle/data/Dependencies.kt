package kr.entree.spigradle.data

import kr.entree.spigradle.SpigradleMeta
import kr.entree.spigradle.spigot.data.SpigotDependencies
import kr.entree.spigradle.util.toFieldEntries

object Dependencies {
    val LOMBOK = Dependency("org.projectlombok", "lombok", "1.18.12")
    val SPIGRADLE = Dependency("kr.entree", "spigradle", SpigradleMeta.VERSION)
}