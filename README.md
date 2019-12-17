# Spigradle

Gradle plugin for Spigot plugin development.

## Apply plugin

Two ways to apply the plugin.

Recommended:

```groovy
plugin {
    id 'kr.entree.spigradle' version '1.0.2'
}
```

The other option:

```groovy
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'kr.entree:spigradle:1.0.2'
    }
}

apply plugin: 'kr.entree.spigradle'
```

## Example

```groovy
plugins {
    id 'java'
    id 'kr.entree.spigradle' version '1.0.2'
}

group 'org.example'
version '1.0-SNAPSHOT'

sourceCompatibility = 1.8

repositories {
    mavenCentral()
}

dependencies {
    compileOnly paper('1.15')
    compileOnly protocolLib()
    testImplementation group: 'junit', name: 'junit', version: '4.12'
}

spigot {
    commands {
        give {
            aliases = ['i']
            description = 'Give command.'
        }
    }
    depend = ['ProtocolLib']
    authors = ['Me']
}
```

We don't need to specify a main class that extends JavaPlugin. Spigradle will find it and generate a plugin.yml automatically.

You can also specify it manually in spigot {} block.

## Requirements

**Spigradle requires Gradle 6.0.1+**

To update gradle wrapper:

```
gradlew wrapper --gradle-version 6.0.1 --distribution-type all
```

## Properties

### repositories

spigot()

bungeecord()

paper()

protocolLib()

jitpack()

### dependencies

spigot()

paper()

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

permission

description

apiVersion

load `STARTUP, POSTWORLD`

website

prefix

### createPluginYaml

attr

encoding
