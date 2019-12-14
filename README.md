# Spigradle

Gradle plugin for Spigot plugin development.

## Example

```groovy
plugins {
    id 'java'
    id 'spigradle'
}

group 'org.example'
version '1.0-SNAPSHOT'

sourceCompatibility = 1.8

repositories {
    mavenCentral()
}

dependencies {
    paper()
    protocolLib()
    testCompile group: 'junit', name: 'junit', version: '4.12'
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

The Spigradle will find your main class that extends JavaPlugin and generate a plugin.yml automatically.

You can also specify your main class manually in spigot {} block.

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
