package kr.entree.spigradle.internal

import java.io.File

/**
 * Created by JunHyung Lim on 2020-05-19
 */
val File.isNotFile get() = !isFile