package kr.entree.spigradle.project
/**
 * Created by JunHyung Lim on 2020-02-29
 */
class Dependency {
    String groupId
    String artifactId
    String defaultVersion
    Closure<String> versionAdjuster
    public static final Closure<String> SPIGOT_VERSION_ADJUSTER = createVersionAdjuster('R0.1', 'SNAPSHOT')
    public static final Closure<String> SNAPSHOT_APPENDER = createVersionAdjuster('SNAPSHOT')
    public static final Closure<String> IDENTITY_ADJUSTER = { String version -> version }

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
            Closure<String> versionAdjuster = IDENTITY_ADJUSTER
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

    static Closure<String> createVersionAdjuster(String... tags) {
        return { String input ->
            if (input == null || input.isBlank()) return null
            def pieces = input.split('-', -1)
            def builder = new StringBuilder()
            builder.with {
                append(input)
                for (int i = pieces.size() - 1; i < tags.size(); i++) {
                    append('-').append(tags[i])
                }
            }
            return builder.toString()
        }
    }
}
