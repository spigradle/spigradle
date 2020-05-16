package kr.entree.spigradle.data

import kr.entree.spigradle.SpigradleMeta

object Dependencies {
    val LOMBOK = Dependency("org.projectlombok", "lombok", "1.18.12")
    val SPIGRADLE = Dependency("kr.entree", "spigradle", SpigradleMeta.VERSION)
}