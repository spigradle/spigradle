package kr.entree.spigradle.data

data class Dependency(
        val group: String,
        val name: String,
        val version: String,
        val versionModifier: (String) -> String = { it }
) {
    fun adjustVersion(inputVersion: String?) = inputVersion?.run(versionModifier) ?: version

    fun format(inputVersion: String? = null): String {
        return "$group:$name:${adjustVersion(inputVersion)}"
    }
}

inline fun Dependency(
        dependency: Dependency,
        group: String = dependency.group,
        name: String = dependency.name,
        version: String = dependency.version,
        noinline versionModifier: (String) -> String = dependency.versionModifier,
        configure: Dependency.() -> Unit = {}
) = Dependency(group, name, version, versionModifier).apply(configure)