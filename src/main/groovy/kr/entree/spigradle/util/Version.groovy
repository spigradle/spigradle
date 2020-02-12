package kr.entree.spigradle.util
/**
 * Created by JunHyung Lim on 2020-02-12
 */
class Version {
    final Integer major
    final Integer minor
    final Integer patch

    Version(Integer major, Integer minor = null, Integer patch = null) {
        this.major = major
        this.minor = minor
        this.patch = patch
    }

    static Version parse(String string) {
        def pieces = string.split('\\.', -1)
        return new Version(
                getIntOrNull(pieces, 0) ?: 0,
                getIntOrNull(pieces, 1),
                getIntOrNull(pieces, 2)
        )
    }

    private static Integer getIntOrNull(String[] array, int index) {
        def element = array.length > index ? array[index] : null
        try {
            return element?.isEmpty() ? 0 : element?.toInteger()
        } catch (NumberFormatException ignored) {
            return null
        }
    }

    @Override
    String toString() {
        def builder = new StringBuilder((major ?: 0).toString())
        def minor = minor != null ? minor : -1
        def patch = patch != null ? patch : -1
        if (minor >= 0) {
            builder.append('.').append(minor)
        }
        if (patch >= 0) {
            builder.append('.').append(patch)
        }
        return builder.toString()
    }
}
