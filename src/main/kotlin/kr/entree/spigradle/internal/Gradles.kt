import org.gradle.api.GradleException

inline fun <T> notNull(any: T?, message: () -> String = { "" }): T {
    return any ?: throw GradleException(message())
}