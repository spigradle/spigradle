# Nukkit Plugin - Spigradle

The [NukkitX](https://github.com/NukkitX/Nukkit#introduction) plugin provides you to:

- Generate 'plugin.yml' with less configuration.

- Shortcuts for dependency and repository.

- Tasks for download and run NukkitX with your plugins for debug.

## Table of contents

- [Requirements](#requirements)

- [Usage](#usage)

- [Main class detection](#main-class-detection)

- [Configuration](#configuration)

- [Tasks](#tasks)

## Requirements

The NukkitX plugin requires Gradle 5.0+, latest version recommended.

To update your gradle wrapper:

```
gradlew wrapper --gradle-version $GRADLE_VERSION --distribution-type all
```

## Usage

[Full Example Here](https://github.com/EntryPointKR/SpigradleSample/tree/master/nukkit)

Groovy DSL

```groovy
plugins {
    id 'kr.entree.spigradle.nukkit' version $SPIGRADLE_VERSION
}
```
Kotlin DSL

```kotlin
plugins {
    id("kr.entree.spigradle.nukkit") version $SPIGRADLE_VERSION
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

apply plugin: 'kr.entree.spigradle.nukkit'
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

apply(plugin = "kr.entree.spigradle.nukkit")
```

</details>

## Main class detection

The Nukkit plugin automatically finds the main class extends [PluginBase](https://ci.nukkitx.com/job/NukkitX/job/Nukkit/job/master/javadoc/index.html?overview-summary.html), and set the 'main' property to the class found.

This is powerful part of Spigradle; if we create a simple plugin that just needs main, name and version in plugin.yml, then we don't need to any configuration. Only pay attention to your unique implementation!  

Also, you can present the main class using `@kr.entree.spigradle.PluginMain`:

```java
import kr.entree.spigradle.PluginMain;

@PluginMain
public class SamplePlugin extends PluginBase { }
```  

## Configuration

### nukkit - [NukkitExtension](TODO Javadoc)

The description of Nukkit plugin that will be generated to a 'plugin.yml'.

About description each property, See [Here](https://github.com/NukkitX/ExamplePlugin/blob/master/src/main/resources/plugin.yml#L1)

<details>
<summary>Groovy Example</summary>

```groovy
nukkit {
    authors 'Me'
    depends 'ProtocolLib', 'Vault'
    api '1.0.5'
    load STARTUP
    commands {
        give {
            aliases 'giv', 'i'
            description 'Give command.'
            permission 'test.foo'
            permissionMessage 'You do not have permission!'
            usage '/<command> [test|stop]'
        }
    }
    permissions {
        'test.foo' {
            description 'Allows foo command'
            defaults 'true'
        }
        'test.*' {
            description 'Wildcard permission'
            defaults 'op'
            children = ['test.foo': true]
        }
    }
}
```

</details>

<details>
<summary>Kotlin Example</summary>

```kotlin
nukkit {
    authors = listOf("Me")
    depends = listOf("SomePlugin")
    api = ['1.0.5']
    load = Load.STARTUP
    commands {
        create("give") {
            aliases = listOf("i")
            description = "Give command."
            permission = "test.foo"
            permissionMessage = "You do not have the** permission!"
            usage = "/<command> [item] [amount]"
        }
    }
    permissions {
        create("test.foo") {
            description = "Allows foo command"
            defaults = "true"
        }
        create("test.*") {
            description = "Wildcard permission"
            defaults = "op"
            children = mapOf("test.foo" to true)
        }
    }
}
```

Without [type-safe accessors](https://docs.gradle.org/current/userguide/kotlin_dsl.html#sec:kotlin_using_standard_api):

```kotlin
configure<NukkitExtension> {
    description = "A NukkitX plugin."
}
```

</details>

## Tasks

All tasks supports [UP-TO-DATE checks](https://docs.gradle.org/current/userguide/more_about_tasks.html#sec:up_to_date_checks).

<details>
<summary>Configuration Guide</summary>

Groovy:

```groovy
runNukkit {
    jvmArgs('-Xmx8G')
}
```

Kotlin with type-safe accessors:

```kotlin
tasks {
    runNukkit {
        jvmArgs("-Xmx8G")
    }
}
```

Kotlin without [type-safe accessors](https://docs.gradle.org/current/userguide/kotlin_dsl.html#sec:kotlin_using_standard_api):

```kotlin
tasks {
    named<JavaExec>("runNukkit") {
        jvmArgs("-Xmx8G")
    }
}
```

Kotlin with property delegation

```kotlin
tasks {
    val runNukkit by existing(JavaExec::clas) {
        jvmArgs("-Xmx8G")
    }
    // Do something with 'runNukkit'
}
```

</details>

### detectNukkitMain - [SubclassDetection](TODO)

Finds the main class extends [cn.nukkit.plugin.PluginBase](https://ci.nukkitx.com/job/NukkitX/job/Nukkit/job/master/javadoc/index.html?overview-summary.html).

### generateNukkitDescription - [YamlGenerate](TODO)

*Depends on: `detectNukkitMain`*

Generates the description file 'plugin.yml'.

### debugNukkit

*Depends on: `prepareNukkitPlugins`, `downloadNukkit`, `runNukkit`*

Downloads NukkitX and runs it with your plugin and dependency plugins.

### prepareNukkitPlugins - [Copy](https://docs.gradle.org/current/dsl/org.gradle.api.tasks.Copy.html)

*Depends on: `build`*

Copies project plugin jar and its dependency plugins into the server plugins directory.

### downloadNukkit - [Download](TODO)

Downloads NukkitX.

### runNukkit - [JavaExec](https://docs.gradle.org/current/dsl/org.gradle.api.tasks.JavaExec.html)

Just runs the NukkitX jar at configured path even there's no executable file.

NOTE: Use `debugNukkit` instead of `runNukkit` if you need prepare process like download NukkitX jar, copy plugins.

### cleanDebug

Deletes all server files.