plugins {
    java
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(kotlin("gradle-plugin", version = "1.4.20"))
    implementation("org.jetbrains.dokka:dokka-gradle-plugin:1.4.32")
    implementation("com.jfrog.bintray.gradle:gradle-bintray-plugin:1.8.5")
    implementation("org.jfrog.buildinfo:build-info-extractor-gradle:4.15.2")
    implementation("gradle.plugin.com.eden:orchidPlugin:0.21.0")
    implementation("com.gradle.publish:plugin-publish-plugin:0.12.0")
}

kotlinDslPluginOptions {
    experimentalWarning.set(false)
}