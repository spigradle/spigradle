plugins {
    java
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(kotlin("gradle-plugin"))
    implementation("org.jetbrains.dokka:dokka-gradle-plugin:2.0.0")
    implementation("gradle.plugin.com.eden:orchidPlugin:0.21.0")
    implementation("com.gradle.publish:plugin-publish-plugin:1.2.1")
}

kotlin {
    compilerOptions {
        // Enable strict compilation mode for build scripts
        allWarningsAsErrors = true
    }
}
