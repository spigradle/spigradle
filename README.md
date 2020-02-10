# Spigradle &middot; [ ![Download](https://api.bintray.com/packages/entrypointkr/Spigradle/spigradle/images/download.svg?version=latest)](https://bintray.com/entrypointkr/Spigradle/spigradle/_latestVersion) [![License](https://img.shields.io/github/license/EntryPointKR/Spigradle.svg)](https://github.com/EntryPointKR/Spigradle/blob/master/LICENSE) 

Gradle plugin for developing Spigot plugin.

## Apply plugin

There is two ways for apply the plugin.

Recommend:

```groovy
plugins {
    id 'kr.entree.spigradle' version '1.1'
}
```

The other option:

```groovy
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'kr.entree:spigradle:1.1'
    }
}

apply plugin: 'kr.entree.spigradle'
```

## Requirements

**Spigradle requires Gradle 5.0+**

To update gradle wrapper:

```
gradlew wrapper --gradle-version 5.6.4 --distribution-type all
```

## Example

```groovy
plugins {
    id 'java'
    id 'kr.entree.spigradle' version '1.1'
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

We don't need to specify a main class that extends JavaPlugin. Spigradle will find it and generate a plugin.yml automatically.

You can also specify it manually in spigot {} block.

**Configuring the spigot {} block is just optional: without it, Spigradle will still work.**

## Properties

### repositories

spigot()

bungeecord()

paper()

protocolLib()

jitpack()

vault() `Same as jitpack()`

enginehub()

### dependencies

spigot() `1.14.4-R0.1-SNAPSHOT = Default version`

paper()

bukkit()

craftbukkit()

protocolLib() `4.4.0`

vault() `1.7`

luckPerms() `5.0`

worldedit() `7.1.0`

worldguard() `7.0.2`

commandhelper() `3.3.4-SNAPSHOT`

### spigot

main

name

version

authors

depends

softDepends

loadBefore

commands

permissions

description

apiVersion

load `STARTUP, POST_WORLD`

website

prefix

### spigotPluginYaml

attributes

encoding

yaml