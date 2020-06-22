import java.net.URL

plugins {
    id("org.jetbrains.dokka")
}

tasks.dokka {
    outputFormat = "html"
    outputDirectory = "$buildDir/kdoc"
    configuration {
        moduleName = project.name
        jdkVersion = 8
        sourceLink {
            path = "src/main/kotlin"
            url = "https://github.com/EntryPointKR/Spigradle/tree/master/src/main/kotlin"
            lineSuffix = "#L"
        }
        externalDocumentationLink {
            url = URL("https://docs.gradle.org/current/javadoc/")
            packageListUrl = URL("https://docs.gradle.org/current/javadoc/package-list")
        }
        externalDocumentationLink {
            url = URL("https://docs.groovy-lang.org/latest/html/gapi/")
            packageListUrl = URL("https://docs.groovy-lang.org/latest/html/gapi/package-list")
        }
        externalDocumentationLink {
            url = URL("https://javadoc.io/doc/com.fasterxml.jackson.dataformat/jackson-dataformat-yaml/latest/")
            packageListUrl = URL("https://javadoc.io/doc/com.fasterxml.jackson.dataformat/jackson-dataformat-yaml/latest/package-list")
        }
        externalDocumentationLink {
            url = URL("https://hub.spigotmc.org/javadocs/spigot/")
            packageListUrl = URL("https://hub.spigotmc.org/javadocs/spigot/package-list")
        }
    }
}

val updatePluginDocs by tasks.registering {
    doLast {
        // TODO
    }
}