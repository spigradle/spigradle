plugins {
    `java-library`
    `kotlin-dsl`
    `java-gradle-plugin`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

// TODO(composite): https://docs.gradle.org/current/userguide/writing_plugins.html#binary_plugins

dependencies {
    // Dependencies for applying external plugins in convention plugins
    // Reference: https://docs.gradle.org/current/samples/sample_sharing_convention_plugins_with_build_logic.html
    implementation("org.jetbrains.dokka:dokka-gradle-plugin:1.9.20")
    implementation("com.gradle.publish:plugin-publish-plugin:1.2.1")
}
