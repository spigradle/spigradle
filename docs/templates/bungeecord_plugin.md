# Bungeecord Plugin - Spigradle

The [Bungeecord](https://www.spigotmc.org/wiki/about-bungeecord/) plugin provides you to:

- Generate 'plugin.yml' with less configuration.

- Shortcuts for dependency and repository.

- Tasks for download and run Bungeecord with your plugins for debug.

## Table of contents

- [Requirements](#requirements)

- [Usage](#usage)

- [Main class detection](#main-class-detection)

- [Configuration](#configuration)

- [Tasks](#tasks)

## Requirements

The Bungeecord plugin requires Gradle 5.0+, latest version recommended.

To update your gradle wrapper:

```
gradlew wrapper --gradle-version $GRADLE_VERSION --distribution-type all
```

## Usage

[Full Example Here](https://github.com/EntryPointKR/SpigradleSample/tree/master/bungeecord)

Groovy DSL

```groovy
plugins {
    id 'kr.entree.spigradle.bungee' version $SPIGRADLE_VERSION
}
```
Kotlin DSL

```kotlin
plugins {
    id("kr.entree.spigradle.bungee") version $SPIGRADLE_VERSION
}
```

<details>
<summary>Groovy Legacy</summary>

```groovy
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'kr.entree:spigradle:$SPIGRADLE_VERSION'
    }
}

apply plugin: 'kr.entree.spigradle.bungee'
```

</details>

<details>
<summary>Kotlin Legacy</summary>

```groovy
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath("kr.entree:spigradle:$SPIGRADLE_VERSION")
    }
}

apply(plugin = "kr.entree.spigradle.bungee")
```

</details>

## Main class detection

The Bungeecord plugin automatically finds the main class extends [Plugin](https://ci.md-5.net/job/BungeeCord/ws/api/target/apidocs/net/md_5/bungee/api/plugin/Plugin.html), and set the 'main' property to the class found.

This is powerful part of Spigradle; if we create a simple plugin that just needs main, name and version in plugin.yml, then we don't need to any configuration. Only pay attention to your unique implementation!  

For performance, you can present the main class using `@kr.entree.spigradle.PluginMain`:

```java
import kr.entree.spigradle.PluginMain;

@PluginMain
public class SamplePlugin extends Plugin { }
```  

## Configuration

### bungee - [BungeeExtension](TODO Javadoc)

The description of Bungeecord plugin that will be generated to a 'bungee.yml'.

About detail, See [Here](https://www.spigotmc.org/wiki/create-your-first-bungeecord-plugin-proxy-spigotmc/#making-it-load)

<details>
<summary>Groovy Example</summary>

```groovy
bungee {
    description 'A Bungeecord plugin.'
    author 'Me'
    depends 'foo', 'bar'
    softDepends 'soft'
}
```

</details>

<details>
<summary>Kotlin Example</summary>

```kotlin
bungee {
    description = "A Bungeecord plugin."
    author = "Me"
    depends = listOf("SomePlugin")
    softDepends = listOf("SomeSoftPlugin")
}
```

Without [type-safe accessors](https://docs.gradle.org/current/userguide/kotlin_dsl.html#sec:kotlin_using_standard_api):

```kotlin
configure<BungeeExtension> {
    description = "A Bungeecord plugin."
}
```

</details>

## Tasks

All tasks supports [UP-TO-DATE checks](https://docs.gradle.org/current/userguide/more_about_tasks.html#sec:up_to_date_checks).

<details>
<summary>Configuration Guide</summary>

Groovy:

```groovy
runBungee {
    jvmArgs('-Xmx8G')
}
```

Kotlin with type-safe accessors:

```kotlin
tasks {
    runBungee {
        jvmArgs("-Xmx8G")
    }
}
```

Kotlin without [type-safe accessors](https://docs.gradle.org/current/userguide/kotlin_dsl.html#sec:kotlin_using_standard_api):

```kotlin
tasks {
    named<JavaExec>("runBungee") {
        jvmArgs("-Xmx8G")
    }
}
```

Kotlin with property delegation

```kotlin
tasks {
    val runBungee by existing(JavaExec::clas) {
        jvmArgs("-Xmx8G")
    }
    // Do something with 'runBungee'
}
```

</details>

### detectBungeeMain - [SubclassDetection](TODO)

Finds the main class extends [net.md_5.bungee.api.plugin.Plugin](https://ci.md-5.net/job/BungeeCord/ws/api/target/apidocs/net/md_5/bungee/api/plugin/Plugin.html).

### generateBungeeDescription - [YamlGenerate](TODO)

*Depends on: `detectBungeeMain`*

Generates the description file 'bungee.yml'.

### debugBungee

*Depends on: `prepareBungeePlugins`, `downloadBungee`, `runBungee`*

Downloads Bungeecord and runs it with your plugin and dependency plugins.

### prepareBungeePlugins - [Copy](https://docs.gradle.org/current/dsl/org.gradle.api.tasks.Copy.html)

*Depends on: `build`*

Copies project plugin jar and its dependency plugins into the server plugins directory.

### downloadBungee - [Download](TODO)

Downloads Bungeecord.

### runBungee - [JavaExec](https://docs.gradle.org/current/dsl/org.gradle.api.tasks.JavaExec.html)

Just runs the Bungeecord jar at configured path even there's no executable file.

NOTE: Use `debugBungee` instead of `runBungee` if you need prepare process like download Bungeecord jar, copy plugins.

### cleanDebug

Deletes all server files.