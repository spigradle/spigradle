# Spigradle &middot; [![License](https://img.shields.io/github/license/EntryPointKR/Spigradle.svg)](https://github.com/EntryPointKR/Spigradle/blob/master/LICENSE)

An intelligent Gradle plugin used to develop plugins for Spigot, Bungeecord and NukkitX.

## Benefits

- Description file generation: `plugin.yml` and/or `bungee.yml`

- Main class detection

- Debug tasks

- Shortcuts for [repository](#repositories) and [dependency](#dependencies)

```groovy
plugins {
    id 'java'
    id 'kr.entree.spigradle' version '$SPIGRADLE_VERSION'
}

group 'org.sample'
version '1.0-SNAPSHOT'

dependencies {
    compileOnly spigot('1.15.2')
}
```

## Table of contents

- [Plugins](#plugins)
- [Requirements](#requirements)
- [Repositories](#repositories)
- [Dependencies](#dependencies)
- [See also](#see-also)
- [Supporters](#supporters)
- [The Spigot plugin](docs/spigot_plugin.md)
- [The Bungeecord plugin](docs/bungeecord_plugin.md)
- [The Nukkit plugin](docs/nukkit_plugin.md)
- [Sample](https://github.com/spigradle/spigradle-sample)

## Plugins

### Spigot

[Documentation](docs/spigot_plugin.md)

#### Demo

- [Groovy - build.gradle](https://github.com/spigradle/spigradle-sample/tree/master/spigot/spigot.gradle)
- [Kotlin - build.gradle.kts](https://github.com/spigradle/spigradle-sample/tree/master/spigot-kotlin/spigot-kotlin.gradle.kts)

Groovy DSL

```groovy
plugins {
    id 'java'
    id 'kr.entree.spigradle' version '$SPIGRADLE_VERSION'
}

dependencies {
    compileOnly spigot('1.15.2')
}
```

<details>
<summary>Kotlin DSL</summary>

```kotlin
import kr.entree.spigradle.kotlin.*

plugins {
    kotlin("jvm") version "$KOTLIN_VERSION"
    id("kr.entree.spigradle") version "$SPIGRADLE_VERSION"
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    compileOnly(spigot("1.15.2"))
}
```

</details>

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

### Bungeecord

[Documentation](docs/bungeecord_plugin.md)

#### Demo
- [Groovy - build.gradle](https://github.com/spigradle/spigradle-sample/tree/master/bungeecord/bungeecord.gradle)
- [Kotlin - build.gradle.kts](https://github.com/spigradle/spigradle-sample/tree/master/bungeecord-kotlin/bungeecord-kotlin.gradle.kts)

Groovy DSL

```groovy
plugins {
    id 'java'
    id 'kr.entree.spigradle.bungee' version '$SPIGRADLE_VERSION'
}

dependencies {
    compileOnly bungeecord('1.15')
}
```

<details>
<summary>Kotlin DSL</summary>

```kotlin
plugins {
    kotlin("jvm") version "$KOTLIN_VERSION"
    id("kr.entree.spigradle.bungee") version "$SPIGRADLE_VERSION"
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    compileOnly(bungeecord("1.15"))
}
```

</details>

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

### NukkitX

[Documentation](docs/nukkit_plugin.md)

#### Demo

- [Groovy - build.gradle](https://github.com/spigradle/spigradle-sample/tree/master/nukkit/nukkit.gradle)
- [Kotlin - build.gradle.kts](https://github.com/spigradle/spigradle-sample/tree/master/nukkit-kotlin/nukkit-kotlin.gradle.kts)

Groovy DSL

```groovy
plugins {
    id 'java'
    id 'kr.entree.spigradle.nukkit' version '$SPIGRADLE_VERSION'
}

dependencies {
    compileOnly nukkit('1.0')
}
```

<details>
<summary>Kotlin DSL</summary>

```kotlin
plugins {
    kotlin("jvm") version "$KOTLIN_VERSION"
    id("kr.entree.spigradle.nukkit") version "$SPIGRADLE_VERSION"
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    compileOnly(bungeecord("1.15"))
}
```

</details>

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

## Requirements

All the plugins requires Gradle 5.4.2+, recommends the latest.

To update your gradle wrapper:

```
gradlew wrapper --gradle-version $GRADLE_VERSION --distribution-type all
```

## Repositories

|  Name         |  URL                                                           | Relations                               | Aliases       |
|---------------|----------------------------------------------------------------|-----------------------------------------|---------------|
| spigotmc()    | https://hub.spigotmc.org/nexus/content/repositories/snapshots/ |                                         | spigot()      |
| sonaytype()   | https://oss.sonatype.org/content/repositories/snapshots/       |                                         | bungeecord()  |
| papermc()     | https://papermc.io/repo/repository/maven-public/               |                                         | paper()       |
| jitpack()     | https://jitpack.io                                             | Vault                                   | vault()       |
| protocolLib() | https://repo.dmulloy2.net/nexus/repository/public/             |                                         |               |
| enginehub()   | https://maven.enginehub.org/repo/                              | worldguard, worldedit, commandhelper... |               |
| codemc()      | https://repo.codemc.org/repository/maven-public/               | BStats                                  | bStats()      |
| enderZone()   | https://ci.ender.zone/plugin/repository/everything/            | EssentialsX                             | essentialsX() |
| frostcast()   | https://ci.frostcast.net/plugin/repository/everything          | BanManager                              | banManager()  |
| nukkitX()     | https://repo.nukkitx.com/maven-snapshots                       | NukkitX                                 |               |

#### Groovy usage

```groovy
repositories {
    engienhub()
}
```

#### Kotiln usage

```kotlin
import kr.entree.spigradle.kotlin.*

repositories {
    enginehub()
}
```

## Dependencies

|  Name             |  Signature                                       | Default version          |
|-------------------|--------------------------------------------------|--------------------------|
| spigot(version)   | org.spigotmc:spigot-api:$version                 | 1.16.1-R0.1-SNAPSHOT     |
| spigotAll()       | org.spigotmc:spigot:$version                     | 1.16.1-R0.1-SNAPSHOT     |
| bungeecord()      | net.md-5:bungeecord-api:$version                 | 1.16-R0.4-SNAPSHOT       |
| minecraftServer() | org.spigotmc:minecraft-server:$version           | 1.16.1-SNAPSHOT          |
| paper()           | com.destroystokyo.paper:paper-api:$version       | 1.16.1-R0.1-SNAPSHOT     |
| bukkit()          | org.bukkit:bukkit:$version                       | 1.16.1-R0.1-SNAPSHOT     |
| craftbukkit()     | org.bukkit:craftbukkit:$version                  | 1.16.1-R0.1-SNAPSHOT     |
| lombok()          | org.projectlombok:lombok:$version                | 1.18.12                  |
| spigradle()       | kr.entree:spigradle:$version                     | $SPIGRADLE_VERSION       |
| protocolLib()     | com.comphenix.protocol:ProtocolLib:$version      | 4.5.1                    |
| vault()           | com.github.MilkBowl:VaultAPI:$version            | 1.7                      |
| vaultAll()        | com.github.MilkBowl:Vault:$version               | 1.7.3                    |
| luckPerms()       | me.lucko.luckperms:luckperms-api:$version        | 5.1                      |
| worldedit()       | com.sk89q.worldedit:worldedit-bukkit:$version    | 7.1.0                    |
| worldguard()      | com.sk89q.worldguard:worldguard-bukkit:$version  | 7.0.3                    |
| essentialsX()     | net.ess3:EssentialsX:$version                    | 2.17.2                   |
| banManager()      | me.confuser.banmanager:BanManagerBukkit:$version | 7.3.0-SNAPSHOT           |
| commandhelper()   | com.sk89q:commandhelper:$version                 | 3.3.4-SNAPSHOT           |
| bStats()          | org.bstats:bstats-bukkit:$version                | 1.7                      |
| bStatsLite()      | org.bstats:bstats-bukkit-lite:$version           | 1.7                      |
| nukkit            | cn.nukkit:nukkit:$version                        | 2.0.0-SNAPSHOT           |

#### Groovy usage

```groovy
dependencies {
    compileOnly spigot("1.15.2") // or just spigot()
}
```

#### Kotlin usage

```kotlin
import kr.entree.spigradle.kotlin.*

dependencies {
    compileOnly(spigot("1.15.2")) // or just spigot()
}
```

## See also

- [The Spigot plugin](docs/spigot_plugin.md)
- [The Bungeecord plugin](docs/bungeecord_plugin.md)
- [The Nukkit plugin](docs/nukkit_plugin.md)
- [Gradle Kotlin DSL Primer](https://docs.gradle.org/current/userguide/kotlin_dsl.html)

## Supporters

<a href="https://www.jetbrains.com/?from=Spigradle"> 
    <img src="assets/jetbrains.svg" alt="JetBrains OS License"/>
</a>
