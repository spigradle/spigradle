package kr.entree.spigradle.project

import org.jetbrains.annotations.Nullable

/**
 * Created by JunHyung Lim on 2020-02-29
 */
class Dependency {
    String groupId
    String artifactId
    String defaultVersion
    Closure<String> versionAdjuster
    public static final Closure<String> SPIGOT_VERSION_ADJUSTER = { String version -> adjustSpigotVersion(version) }
    public static final Closure<String> NULL_ADJUSTER = { String version -> null }

    Dependency(String groupId, String artifactId, String defaultVersion, Closure<String> versionAdjuster) {
        this.groupId = groupId
        this.artifactId = artifactId
        this.defaultVersion = defaultVersion
        this.versionAdjuster = versionAdjuster
    }

    static Dependency dependency(
            String groupId,
            String artifactId,
            String defaultVersion,
            Closure<String> versionAdjuster = NULL_ADJUSTER
    ) {
        return new Dependency(groupId, artifactId, defaultVersion, versionAdjuster)
    }

    static Dependency dependency(
            Dependency parent,
            @DelegatesTo(Dependency) Closure configure
    ) {
        def child = new Dependency(parent.groupId, parent.artifactId, parent.defaultVersion, parent.versionAdjuster)
        configure.rehydrate(child, this, this).call()
        return child
    }

    String format(String version) {
        return "$groupId:$artifactId:${versionAdjuster.call(version) ?: this.defaultVersion}"
    }

    Closure<String> formatter() {
        return { String version -> format(version) }
    }

    @Nullable
    static String adjustSpigotVersion(String string) {
        if (string == null || string.isBlank()) return null
        def pieces = string.split('-')
        def builder = new StringBuilder()
        builder.with {
            append(string)
            if (pieces.length <= 1) {
                append('-').append("R0.1")
            }
            if (pieces.length <= 2) {
                append('-').append("SNAPSHOT")
            }
        }
        return builder.toString()
    }
}
