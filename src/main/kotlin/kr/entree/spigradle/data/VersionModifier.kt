package kr.entree.spigradle.data

object VersionModifier {
    val SNAPSHOT_APPENDER = createAdjuster("SNAPSHOT")
    val SPIGOT_ADJUSTER = createAdjuster("R0.1", "SNAPSHOT")

    fun createAdjuster(vararg tags: String): (String) -> String = { version ->
        buildString {
            val pieces = version.split("-")
            append(version)
            tags.drop(pieces.size - 1).forEach {
                append("-").append(it)
            }
        }
    }
}