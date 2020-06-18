# Spigot plugin - Spigradle

The [Spigot](https://www.spigotmc.org/wiki/about-spigot/) plugin provides you to:

- Generate 'plugin.yml' with less configuration.

- Shortcuts for dependency and repository.

- Tasks for build and run Spigot with your plugins for debug.

## Table of contents

- [Requirements](#requirements)

- [Usage](#usage)

- [Main class detection](#main-class-detection)

- [Configuration](#configuration)

- [Tasks](#tasks)

- [Why not spigot-annotations?](#why-not-spigot-annotations)

- [Testing with MockBukkit](#testing-with-mockbukkit)

## Requirements

The Spigot plugin requires Gradle 5.0+, latest version recommended.

To update your gradle wrapper:

```
gradlew wrapper --gradle-version $GRADLE_VERSION --distribution-type all
```

## Usage

[Full Example Here](https://github.com/EntryPointKR/SpigradleSample/tree/master/spigot)

Groovy DSL

```groovy
plugins {
    id 'kr.entree.spigradle' version $SPIGRADLE_VERSION
}
```
Kotlin DSL

```kotlin
plugins {
    id("kr.entree.spigradle") version $SPIGRADLE_VERSION
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

apply plugin: 'kr.entree.spigradle'
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

apply(plugin = "kr.entree.spigradle")
```

</details>

## Main class detection

The Spigot plugin automatically finds the main class extends [JavaPlugin]((https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/plugin/java/JavaPlugin.html)), and set the 'main' property to the class found.

This is powerful part of Spigradle; if we create a simple plugin that just needs main, name and version in plugin.yml, then we don't need to any configuration. Only pay attention to your unique implementation!  

For performance, you can present the main class using `@kr.entree.spigradle.PluginMain`:

```java
import kr.entree.spigradle.PluginMain;

@PluginMain
public class SamplePlugin extends JavaPlugin { }
```  

In Kotlin, you can use `@Plugin` typealias linked to `@PluginMain`.

```kotlin
@Plugin
class SamplePlugin : JavaPlugin()
``` 

The reason of naming to `@PluginMain` instead of `@Plugin`, the name `Plugin` already used at Bungeecord and Nukkit. For support those, I had to avoid the name conflict. But the name conflict doesn't a matter in Kotlin, so I provided the `@Plugin` typealias for Kotlin user.

## Configuration

### spigot - [SpigotExtension](TODO Javadoc)

The description of Spigot plugin that will be generated to a 'plugin.yml'.

The 'main' property will be set to auto-detected or presented by `@kr.entree.spigradle.PluginMain`.

About detail, See [Here](https://www.spigotmc.org/wiki/plugin-yml/)

<details>
<summary>Groovy Example</summary>

```groovy
spigot {
    authors = ['Me']
    depends = ['ProtocolLib']
    apiVersion = '1.15'
    load = STARTUP
    commands {
        give {
            aliases = listOf('i')
            description = 'Give command.'
            permission = 'test.foo'
            permissionMessage = 'You do not have permission!'
            usage = '/<command> [test|stop]'
        }
    }
    permissions {
        'test.foo' {
            description = 'Allows foo command'
            defaults = 'true'
        }
        'test.*' {
            description = 'Wildcard permission'
            defaults = 'op'
            children = ['test.foo': true]
        }
    }
}
```

</details>

<details>
<summary>Kotlin Example</summary>

```kotlin
spigot {
    authors = listOf("Me")
    depends = listOf("ProtocolLib")
    apiVersion = "1.15"
    load = Load.STARTUP
    commands {
        create("give") {
            aliases = listOf("i")
            description = "Give command."
            permission = "test.foo"
            permissionMessage = "You do not have permission!"
            usage = "/<command> [test|stop]"
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
configure<SpigotExtension> {
    authors = listOf("Me")
}
```

</details>

## Tasks

All tasks supports [UP-TO-DATE checks](https://docs.gradle.org/current/userguide/more_about_tasks.html#sec:up_to_date_checks).

<details>
<summary>Configuration Guide</summary>

Groovy:

```groovy
runSpigot {
    jvmArgs('-Xmx8G')
}
```

Kotlin with type-safe accessors:

```kotlin
tasks {
    runSpigot {
        jvmArgs("-Xmx8G")
    }
}
```

Kotlin without [type-safe accessors](https://docs.gradle.org/current/userguide/kotlin_dsl.html#sec:kotlin_using_standard_api):

```kotlin
tasks {
    named<JavaExec>("runSpigot") {
        jvmArgs("-Xmx8G")
    }
}
```

Kotlin with property delegation

```kotlin
tasks {
    val runSpigot by existing(JavaExec::clas) {
        jvmArgs("-Xmx8G")
    }
    // Do something with 'runSpigot'
}
```

</details>

### detectSpigotMain - [SubclassDetection](TODO)

Finds the main class extends [org.bukkit.plugin.java.JavaPlugin](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/plugin/java/JavaPlugin.html).

### generateSpigotDescription - [YamlGenerate](TODO)

*Depends on: `detectSpigotMain`*

Generates the description file 'plugin.yml'.

### debugSpigot

*Depends on: `prepareSpigotPlugins`, `prepareSpigot`, `runSpigot`*

Builds Spigot and runs it with your plugin and dependency plugins.

### debugPaper

*Depends on: `prepareSpigotPlugins`, `downloadPaper`, `runSpigot`*

Runs Paper with your plugin and dependency plugins.

### prepareSpigotPlugins - [Copy](https://docs.gradle.org/current/dsl/org.gradle.api.tasks.Copy.html)

*Depends on: `build`*

Copies project plugin jar and its dependency plugins into the server plugins directory.

### prepareSpigot - [Copy](https://docs.gradle.org/current/dsl/org.gradle.api.tasks.Copy.html)

*Depends on: `downloadSpigotBuildTools`, `buildSpigot`*

Prepares Spigot for ready to run.

### runSpigot - [JavaExec](https://docs.gradle.org/current/dsl/org.gradle.api.tasks.JavaExec.html)

Just runs the server jar at configured path even there's no executable file.

NOTE: Use `debugSpigot` or `debugPaper` instead of `runSpigot` if you need prepare process like download server jar, copy plugins.

### downloadSpigotBuildTools - [Download](TODO)

Downloads Spigot BuildTools.

### buildSpigot - [JavaExec](https://docs.gradle.org/current/dsl/org.gradle.api.tasks.JavaExec.html)

Builds Spigot using BuildTools.

### downloadPaper - [Download](TODO)

Downloads Paperclip.

### cleanSpigotBuild

Deletes all build outputs created by Spigot BuildTools.

### cleanDebug

Deletes all server files.

## Why not spigot-annotations?

```kotlin
@Plugin(name = "SamplePlugin", version = "1.0-SNAPSHOT")
@Commands(@Command(name = "foo", desc = "Foo command", aliases = ["foobar", "fubar"], permission = "test.foo", permissionMessage = "You do not have permission!", usage = "/<command> [test|stop]"))
@Permission(name = "test.foo", desc = "Allows foo command", defaultValue = PermissionDefault.OP)
class SamplePlugin : JavaPlugin()
```

If you use Gradle, the following reasons why no [spigot-annotations](https://hub.spigotmc.org/stash/projects/SPIGOT/repos/plugin-annotations/browse):

1. Duplicate information of the `name` and `version`, those already provided in `build.gradle`.
2. The Annotation doesn't suit for providing complex information like `commands` and `permissions`.
3. Using Gradle, we can configure dynamically all things.

Replacement on build.gradle, not on runtime code:

```groovy
spigot {
    // The 'name' and 'version' will be set to project.version and project.name, 
    // But we can set those manually.
    name = 'Manual name'
    version = 'Manual version'
    commands {
        foo {
            aliases = ['foobar', 'fubar']
            description = 'Foo command'
            permission = 'test.foo'
            permissionMessage = 'You do not have permission!'
            usage = '/<command> [test|stop]'
        }
    }
    permissions {
        'test.foo' {
            description = 'Allows foo command'
            defaults = 'op'
        }
    }
}
```

## Testing with MockBukkit

With Spigradle 1.3.0+, you can use the [MockBukkit](https://github.com/seeseemelk/MockBukkit) without any special code.

If you use less than 1.3.0, add it in build.gradle:

```groovy
task copyPluginYaml(type: Copy, dependsOn: spigotPluginYaml) {
    from(new File(spigotPluginYaml.temporaryDir, 'plugin.yml'))
    into(sourceSets.test.output.resourcesDir)
}

tasks.test.dependsOn(copyPluginYaml)
```