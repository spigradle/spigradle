plugins {
    java
    `kotlin-dsl`
}

repositories {
    jcenter()
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(kotlin("gradle-plugin", version = "1.3.72"))
    implementation("org.jetbrains.dokka:dokka-gradle-plugin:0.10.0")
    implementation("com.jfrog.bintray.gradle:gradle-bintray-plugin:1.8.5")
    implementation("org.jfrog.buildinfo:build-info-extractor-gradle:4.15.2")
    implementation("gradle.plugin.com.eden:orchidPlugin:0.21.0")
    implementation("com.gradle.publish:plugin-publish-plugin:0.12.0")
}

kotlinDslPluginOptions {
    experimentalWarning.set(false)
}