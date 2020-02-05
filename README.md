# Spigradle &middot; [ ![Download](https://api.bintray.com/packages/entrypointkr/Spigradle/spigradle/images/download.svg?version=latest) ](https://bintray.com/entrypointkr/Spigradle/spigradle/_latestVersion) [![License](https://img.shields.io/github/license/EntryPointKR/Spigradle.svg)](https://github.com/EntryPointKR/Spigradle/blob/master/LICENSE) 

Gradle plugin for developing Spigot plugin.

## Apply plugin

Two ways to apply the plugin.

Recommended:

```groovy
plugins {
    id 'kr.entree.spigradle' version '1.0.6'
}
```

The other option:

```groovy
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'kr.entree:spigradle:1.0.6'
    }
}

apply plugin: 'kr.entree.spigradle'
```

## Requirements

**Spigradle requires Gradle 6.0.1+**

To update gradle wrapper:

```
gradlew wrapper --gradle-version 6.0.1 --distribution-type all
```

## Example

```groovy
plugins {
    id 'java'
    id 'kr.entree.spigradle' version '1.0.6'
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
    testImplementation group: 'junit', name: 'junit', version: '4.12'
}

spigot {
    commands {
        give {
            aliases = ['i']
            description = 'Give command.'
        }
    }
    depend = ['ProtocolLib', 'Vault']
    authors = ['Me']
}
```

We don't need to specify a main class that extends JavaPlugin. Spigradle will find it and generate a plugin.yml automatically.

You can also specify it manually in spigot {} block.

## Properties

### repositories

spigot()

bungeecord()

paper()

protocolLib()

jitpack()

vault() `Same as jitpack()`

### dependencies

spigot()

paper()

bukkit()

craftbukkit()

protocolLib()

vault()

### spigot

main

name

version

authors

depend

softDepend

loadBefore

commands

permissions

description

apiVersion

load `STARTUP, POSTWORLD`

website

prefix

### createPluginYaml

attr

encoding
