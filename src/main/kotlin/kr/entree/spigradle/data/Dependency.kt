package kr.entree.spigradle.data

data class Dependency(
        val group: String,
        val name: String,
        val version: String
)

inline fun Dependency(
        dependency: Dependency,
        group: String = dependency.group,
        name: String = dependency.name,
        version: String = dependency.version,
        configure: Dependency.() -> Unit = {}
) = Dependency(group, name, version).apply(configure)

fun Dependency.format(version: String): String {
    TODO()
}