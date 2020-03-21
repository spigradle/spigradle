# Spigradle &middot; [ ![Download](https://api.bintray.com/packages/entrypointkr/Spigradle/spigradle/images/download.svg?version=latest)](https://bintray.com/entrypointkr/Spigradle/spigradle/_latestVersion) [![License](https://img.shields.io/github/license/EntryPointKR/Spigradle.svg)](https://github.com/EntryPointKR/Spigradle/blob/master/LICENSE) 

Gradle plugin for developing Spigot plugin.

## Table of contents

- [Apply plugin](#apply-plugin)
- [Requirements](#requirements)
- [Full Example](#full-example)
- [Repositories](#repositories)
- [Dependencies](#dependencies)
- [Extensions](#extensions)
  - [spigot](#spigot)
- [Tasks](#tasks)
  - [spigotPluginYaml](#spigotpluginyaml)
- [Supporters](#suppoters)

## Apply plugin

There is two ways for apply the plugin.

Recommend:

```groovy
plugins {
    id 'kr.entree.spigradle' version '1.2.2'
}
```

<details>
<summary>Kotlin</summary>

```kotlin
plugins {
    id("kr.entree.spigradle") version "1.2.2"
}
```

</details>

The other option:

```groovy
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'kr.entree:spigradle:1.2.2'
    }
}

apply plugin: 'kr.entree.spigradle'
```

<details>
<summary>Kotlin</summary>

```kotlin
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath("kr.entree:spigradle:1.2.2")
    }
}

apply(plugin = "kr.entree.spigradle")
```

</details>

## Requirements

**Spigradle requires Gradle 5.0+**

IntelliJ's default gradle wrapper already 5.2.1.

If you want fully IDE support in DSL, just go to 6.0+ (currently latest 6.2.2)

```
gradlew wrapper --gradle-version 6.2.2 --distribution-type all
```

## Full Example

<details>
<summary>Groovy</summary>
<p>

```groovy
plugins {
    id 'java'
    id 'kr.entree.spigradle' version '1.2.2'
}

group 'org.example'
version '1.0-SNAPSHOT'

sourceCompatibility = 1.8

repositories {
    mavenCentral()
    protocolLib()
    jitpack() // For vault
}

dependencies {
    compileOnly paper('1.15.1') // Or spigot()
    compileOnly protocolLib()
    compileOnly vault()
    testImplementation 'junit:junit:4.12'
}

spigot {
    authors = ['Me']
    depends = ['ProtocolLib']
    apiVersion = '1.15'
    load = STARTUP
    commands {
        give {
            aliases = ['i']
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

</p>
</details>

<details>
<summary>Kotlin</summary>
<p>

```kotlin
import kr.entree.spigradle.attribute.*
import kr.entree.spigradle.kotlin.*

plugins {
    kotlin("jvm") version "1.3.70"
    id("kr.entree.spigradle") version "1.2.2"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    protocolLib()
    jitpack() // For vault
}

dependencies {
    compileOnly(paper("1.15.1")) // Or spigot()
    compileOnly(protocolLib())
    compileOnly(vault())
    testImplementation("junit:junit:4.12")
}

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

</p>
</details>

Spigradle can smartly generate a plugin.yml.

In plugin.yml properties, spigradle will set:

- main: Spigradle will find a main class that extends JavaPlugin (or presented by @kr.entree.spigradle.Plugin annotation) in your project.
- name: project.name in gradle.
- version: project.version in gradle.

So if you create a simple plugin that just uses main, name, version in plugin.yml, then just apply the Spigradle without configuring and you're all set.

Also, we can manually set these properties in [spigot {} block](https://github.com/EntryPointKR/Spigradle#spigot).

## Repositories

|  Name         |  URL                                                           | Relations                               | Aliases  |
|---------------|----------------------------------------------------------------|-----------------------------------------|----------|
| spigot()      | https://hub.spigotmc.org/nexus/content/repositories/snapshots/ |                                         |          |
| bungeecord()  | https://oss.sonatype.org/content/repositories/snapshots/       |                                         |          |
| paper()       | https://papermc.io/repo/repository/maven-public/               |                                         |          |
| jitpack()     | https://jitpack.io                                             | Vault                                   | vault()  |
| protocolLib() | https://repo.dmulloy2.net/nexus/repository/public/             |                                         |          |
| enginehub()   | https://maven.enginehub.org/repo/                              | worldguard, worldedit, commandhelper... |          |
| codemc()      | https://repo.codemc.org/repository/maven-public/               | BStats                                  | bStats() |

## Dependencies

|  Name             |  Signature                                      | Default version          |
|-------------------|-------------------------------------------------|--------------------------|
| spigot(version)   | org.spigotmc:spigot-api:$version                | 1.15.2-R0.1-SNAPSHOT     |
| spigotAll()       | org.spigotmc:spigot:$version                    | 1.15.2-R0.1-SNAPSHOT     |
| bungeecord()      | net.md-5:bungeecord-api:$version                | 1.15-SNAPSHOT
| minecraftServer() | org.spigotmc:minecraft-server:$version          | 1.15.2-SNAPSHOT          |
| paper()           | com.destroystokyo.paper:paper-api:$version      | 1.15.2-R0.1-SNAPSHOT     |
| bukkit()          | org.bukkit:bukkit:$version                      | 1.15.2-R0.1-SNAPSHOT     |
| craftbukkit()     | org.bukkit:craftbukkit:$version                 | 1.15.2-R0.1-SNAPSHOT     |
| lombok()          | org.projectlombok:lombok:$version               | 1.18.12                  |
| spigradle()       | kr.entree:spigradle:$version                    | What version you applied |
| protocolLib()     | com.comphenix.protocol:ProtocolLib:$version     | 4.4.0                    |
| vault()           | com.github.MilkBowl:VaultAPI:$version           | 1.7                      |
| luckPerms()       | me.lucko.luckperms:luckperms-api:$version       | 5.0                      |
| worldedit()       | com.sk89q.worldedit:worldedit-bukkit:$version   | 7.1.0                    |
| worldguard()      | com.sk89q.worldguard:worldguard-bukkit:$version | 7.0.2                    |
| commandhelper()   | com.sk89q:commandhelper:$version                | 3.3.4-SNAPSHOT           |
| bStats()          | org.bstats:bstats-bukkit:$version               | 1.7                      |
| bStatsLite()      | org.bstats:bstats-bukkit-lite:$version          | 1.7                      |

## Extensions

### spigot

| Name        | Description                                    |
|-------------|------------------------------------------------|
| main        | See: https://www.spigotmc.org/wiki/plugin-yml/ |
| name        |                                                |
| version     |                                                |
| description |                                                |
| website     |                                                |
| authors     |                                                |
| apiVersion  | 1.13 or 1.14 or 1.15...                        |
| load        | Load.POSTWORLD or Load.STARTUP                 |
| prefix      |                                                |
| depends     |                                                |
| softDepends |                                                |
| loadBefore  |                                                |
| commands    | See Full Example                               |
| permissions |                                                |

## Tasks

### spigotPluginYaml

| Name         | Description                                   |
|--------------|-----------------------------------------------|
| attributes   | Attributes for plugin.yml                     |
| encoding     | Encoding for plugin.yml                       |
| yaml         | For customizing yaml style                    |
| includeTasks | Where plugin.yml should be included, excluded |

## Suppoters

<a href="https://www.jetbrains.com/?from=Spigradle"> 
    <img src="assets/jetbrains.svg" alt="JetBrains OS License"/>
</a>