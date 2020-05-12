package kr.entree.spigradle.util

import kotlin.random.Random

/**
 * Created by JunHyung Lim on 2020-05-12
 */
fun Random.nextString(
        size: Int,
        charRange: CharRange = 'a'..'z'
) = buildString {
    repeat(size) { charRange.random() }
}