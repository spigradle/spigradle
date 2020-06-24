# Spigradle

[comment]: <> (!! Do not edit this file but 'docs/templates' or 'docs/root-templates', See [CONTRIBUTING.md] !!)

An intelligent Gradle plugin used to develop plugins for Spigot, Bungeecord and NukkitX.

- Description file generation: `plugin.yml` or `bungee.yml`

- Main class detection

- Debug tasks

- Shortcuts for repository and dependency

## Table of contents

[comment]: <> (!! Do not edit this file but 'docs/templates' or 'docs/root-templates', See [CONTRIBUTING.md] !!)

- [Plugins](#plugins)
- [Requirements](#requirements)
- [Dependencies](#dependencies)
- [Repositories](#repositories)
- [See also](#see-also)
- [Supporters](#supporters)
- [The Spigot plugin](docs/spigot_plugin.md)
- [The Bungeecord plugin](docs/bungeecord_plugin.md)
- [The Nukkit plugin](docs/nukkit_plugin.md)
- [Sample](https://github.com/EntryPointKR/SpigradleSample)

## Plugins

[comment]: <> (!! Do not edit this file but 'docs/templates' or 'docs/root-templates', See [CONTRIBUTING.md] !!)

### Spigot

[comment]: <> (!! Do not edit this file but 'docs/templates' or 'docs/root-templates', See [CONTRIBUTING.md] !!)

[Documentation](docs/spigot_plugin.md)

[Demo](https://github.com/EntryPointKR/SpigradleSample/tree/master/spigot)

Groovy DSL

```groovy
plugins {
    id 'kr.entree.spigradle' version '1.3.0-SNAPSHOT'
}
```

<details>
<summary>Kotlin DSL</summary>

```kotlin
plugins {
    id("kr.entree.spigradle") version "1.3.0-SNAPSHOT"
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
        classpath 'kr.entree:spigradle:1.3.0-SNAPSHOT'
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
        classpath("kr.entree:spigradle:1.3.0-SNAPSHOT")
    }
}

apply(plugin = "kr.entree.spigradle")
```

</details>

### Bungeecord

[comment]: <> (!! Do not edit this file but 'docs/templates' or 'docs/root-templates', See [CONTRIBUTING.md] !!)

[Documentation](docs/spigot_plugin.md)

[Demo](https://github.com/EntryPointKR/SpigradleSample/tree/master/bungeecord)

Groovy DSL

```groovy
plugins {
    id 'kr.entree.spigradle.bungee' version '1.3.0-SNAPSHOT'
}
```

<details>
<summary>Kotlin DSL</summary>

```kotlin
plugins {
    id("kr.entree.spigradle.bungee") version "1.3.0-SNAPSHOT"
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
        classpath 'kr.entree:spigradle:1.3.0-SNAPSHOT'
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
        classpath("kr.entree:spigradle:1.3.0-SNAPSHOT")
    }
}

apply(plugin = "kr.entree.spigradle.bungee")
```

</details>

### NukkitX

[comment]: <> (!! Do not edit this file but 'docs/templates' or 'docs/root-templates', See [CONTRIBUTING.md] !!)

[Documentation](docs/nukkit_plugin.md)

[Demo](https://github.com/EntryPointKR/SpigradleSample/tree/master/nukkit)

Groovy DSL

```groovy
plugins {
    id 'kr.entree.spigradle.nukkit' version '1.3.0-SNAPSHOT'
}
```

<details>
<summary>Kotlin DSL</summary>

```kotlin
plugins {
    id("kr.entree.spigradle.nukkit") version "1.3.0-SNAPSHOT"
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
        classpath 'kr.entree:spigradle:1.3.0-SNAPSHOT'
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
        classpath("kr.entree:spigradle:1.3.0-SNAPSHOT")
    }
}

apply(plugin = "kr.entree.spigradle.nukkit")
```

</details>

## Requirements

[comment]: <> (!! Do not edit this file but 'docs/templates' or 'docs/root-templates', See [CONTRIBUTING.md] !!)

All the plugins requires Gradle 5.0+, recommends the latest.

To update your gradle wrapper:

```
gradlew wrapper --gradle-version 6.4.1 --distribution-type all
```

## Repositories

[comment]: <> (!! Do not edit this file but 'docs/templates' or 'docs/root-templates', See [CONTRIBUTING.md] !!)

|  Name         |  URL                                                           | Relations                               | Aliases       |
|---------------|----------------------------------------------------------------|-----------------------------------------|---------------|
| spigot()      | https://hub.spigotmc.org/nexus/content/repositories/snapshots/ |                                         |               |
| bungeecord()  | https://oss.sonatype.org/content/repositories/snapshots/       |                                         |               |
| paper()       | https://papermc.io/repo/repository/maven-public/               |                                         |               |
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

[comment]: <> (!! Do not edit this file but 'docs/templates' or 'docs/root-templates', See [CONTRIBUTING.md] !!)

|  Name             |  Signature                                       | Default version          |
|-------------------|--------------------------------------------------|--------------------------|
| spigot(version)   | org.spigotmc:spigot-api:$version                 | 1.15.2-R0.1-SNAPSHOT     |
| spigotAll()       | org.spigotmc:spigot:$version                     | 1.15.2-R0.1-SNAPSHOT     |
| bungeecord()      | net.md-5:bungeecord-api:$version                 | 1.15-SNAPSHOT            |
| minecraftServer() | org.spigotmc:minecraft-server:$version           | 1.15.2-SNAPSHOT          |
| paper()           | com.destroystokyo.paper:paper-api:$version       | 1.15.2-R0.1-SNAPSHOT     |
| bukkit()          | org.bukkit:bukkit:$version                       | 1.15.2-R0.1-SNAPSHOT     |
| craftbukkit()     | org.bukkit:craftbukkit:$version                  | 1.15.2-R0.1-SNAPSHOT     |
| lombok()          | org.projectlombok:lombok:$version                | 1.18.12                  |
| spigradle()       | kr.entree:spigradle:$version                     | 1.3.0-SNAPSHOT       |
| protocolLib()     | com.comphenix.protocol:ProtocolLib:$version      | 4.4.0                    |
| vault()           | com.github.MilkBowl:VaultAPI:$version            | 1.7                      |
| vaultAll()        | com.github.MilkBowl:Vault:$version               | 1.7.2                    |
| luckPerms()       | me.lucko.luckperms:luckperms-api:$version        | 5.1                      |
| worldedit()       | com.sk89q.worldedit:worldedit-bukkit:$version    | 7.1.0                    |
| worldguard()      | com.sk89q.worldguard:worldguard-bukkit:$version  | 7.0.2                    |
| essentialsX()     | net.ess3:EssentialsX:$version                    | 2.17.2                   |
| banManager()      | me.confuser.banmanager:BanManagerBukkit:$version | 7.1.0-SNAPSHOT           |
| commandhelper()   | com.sk89q:commandhelper:$version                 | 3.3.4-SNAPSHOT           |
| bStats()          | org.bstats:bstats-bukkit:$version                | 1.7                      |
| bStatsLite()      | org.bstats:bstats-bukkit-lite:$version           | 1.7                      |
| nukkit            | cn.nukkit:nukkit:$version                        | 1.0-SNAPSHOT             |

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

[comment]: <> (!! Do not edit this file but 'docs/templates' or 'docs/root-templates', See [CONTRIBUTING.md] !!)

- [The Spigot plugin](docs/spigot_plugin.md)
- [The Bungeecord plugin](docs/bungeecord_plugin.md)
- [The Nukkit plugin](docs/nukkit_plugin.md)
- [Gradle Kotlin DSL Primer](https://docs.gradle.org/current/userguide/kotlin_dsl.html)

## Supporters

[comment]: <> (!! Do not edit this file but 'docs/templates' or 'docs/root-templates', See [CONTRIBUTING.md] !!)

<a href="https://www.jetbrains.com/?from=Spigradle"> 
    <img src="assets/jetbrains.svg" alt="JetBrains OS License"/>
</a>
