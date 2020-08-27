# [2.2.0](https://github.com/spigradle/spigradle/compare/v2.1.2...v2.2.0) (2020-08-27)


### Bug Fixes

* **debug-run:** fix again `configSpigot` ([2a5e034](https://github.com/spigradle/spigradle/commit/2a5e03429602ca0ffc106e5596ef9b41acf09cd5))
* **debug-run:** ignore exception for `configSpigot` ([696eee7](https://github.com/spigradle/spigradle/commit/696eee7cd8ea16edc9e8411815040ec94f1667ee))
* **debug-run:** NoPluginFoundException for `preparePlugins` ([42a1789](https://github.com/spigradle/spigradle/commit/42a1789a42a9872f3db44a2e107592574da957d9))
* apt output file name, dep resolution test condition ([9f15100](https://github.com/spigradle/spigradle/commit/9f15100e5d70033d333c46b4d36e22c3ade07c53))
* compile error ([0dbf677](https://github.com/spigradle/spigradle/commit/0dbf677104e94f8f9993665bcc7a5caa80eae70a))
* create `prepare$name` task for bungee, nukkit [#31](https://github.com/spigradle/spigradle/issues/31) ([6bec2cb](https://github.com/spigradle/spigradle/commit/6bec2cb4a37467005aba102b024d09e9d4955be7))
* find both tasks `preparePlugins`, `preparePlugin` ([082d73a](https://github.com/spigradle/spigradle/commit/082d73aa3cdf258ece8814c9d2deb2f369dc3163))
* support apply multi plugin spigot, bungee ([6e80231](https://github.com/spigradle/spigradle/commit/6e80231fb1e0a2bcc9b663b727112597b322200b))
* transient debug field for bungee, nukkit [#32](https://github.com/spigradle/spigradle/issues/32) ([f4b3a82](https://github.com/spigradle/spigradle/commit/f4b3a822cbf02b4ab5f95055fae0c3cb040a7599))


### Features

* new task `configSpigot` ([7649368](https://github.com/spigradle/spigradle/commit/76493685004cd2b7a47e12847e62c795b8586e65))
* set default the `description` to `project.description` ([89c8ead](https://github.com/spigradle/spigradle/commit/89c8ead1ca5e8b882e473a579ee42f2081c805a5))
* **spigot:** add `serverPort` in SpigotDebug ([2e77a2c](https://github.com/spigradle/spigradle/commit/2e77a2c07663b29f1c0b8df9540376f964e53e7a))
* **spigot:** add groovy helper for the `serverPort` ([73b2714](https://github.com/spigradle/spigradle/commit/73b27142b4e751d839694cde4c636150c5623a13))

## [2.1.2](https://github.com/spigradle/spigradle/compare/v2.1.1...v2.1.2) (2020-08-21)


### Bug Fixes

* make debug tasks depends on `assemble` instead of `build` for avoid test ([a52b0a1](https://github.com/spigradle/spigradle/commit/a52b0a1b33ace660dd486827710145c0996b48d2))
* only apply IdeaPlugin on rootProject for scala ([01f3496](https://github.com/spigradle/spigradle/commit/01f3496198be209e0dcfba32946820f5e1d64a60))
* **debug-run:** transitive `preparePlugins` task ([5cb321e](https://github.com/spigradle/spigradle/commit/5cb321e7f9110b5bb8b94241ee7a6f1fbb8ccca7))

## [2.1.1](https://github.com/spigradle/spigradle/compare/v2.1.0...v2.1.1) (2020-07-24)


### Bug Fixes

* **debug-run:** task `prepareSpigotPlugins` depends on `assemble` instead of `build` ([28c2f7f](https://github.com/spigradle/spigradle/commit/28c2f7f5b1cdd730cc0db88226211e4f544f3d73))

# [2.1.0](https://github.com/spigradle/spigradle/compare/v2.0.1...v2.1.0) (2020-07-23)


### Bug Fixes

* improve eula accepter to cover disagreed case ([10bfffa](https://github.com/spigradle/spigradle/commit/10bfffa84aea886cdec22cfeba4f7820b0ae7f81))
* **deps:** bump up mc version to 1.16.1 ([0154ecc](https://github.com/spigradle/spigradle/commit/0154ecc1fc170a333afd80bcfe64181c7fdf653e))
* check File#isFile for multi-situation in SubclassDetection ([809020c](https://github.com/spigradle/spigradle/commit/809020cf6619d2d610aef62170faf9b1f743754b))


### Features

* **debug-run:** add eula accept gradle task for general purpose ([4935493](https://github.com/spigradle/spigradle/commit/4935493c43f43e14985549b02968398741f65657))
* **debug-run:** add groovy helper for programArgs, jvmArgs ([891740b](https://github.com/spigradle/spigradle/commit/891740bf41b4f6c12eb673e6716e252d76a13dc1))
* **debug-run:** add properties `programArgs` and `vmArgs` in Debug configuration ([e5a4c9e](https://github.com/spigradle/spigradle/commit/e5a4c9e2a848428123b40993c9aed1a39f6bee48))
* **ide-idea:** add ability to generate Paper JarApp RunConfiguration ([78b9076](https://github.com/spigradle/spigradle/commit/78b9076ba5ab95d28b3282976e4d643e74d9a658))
* add ability to generate `RunServer` JarApp RunConfiguration for IDEA ([9b3a73b](https://github.com/spigradle/spigradle/commit/9b3a73b5f1e438a5b3dada92ee44928f25b2d34b))
* generate server.jar run configuration for IDEA ([496817b](https://github.com/spigradle/spigradle/commit/496817b41418fab685c514fd428b3a65b6927c29))

## [2.0.1](https://github.com/spigradle/spigradle/compare/v2.0.0...v2.0.1) (2020-07-08)


### Bug Fixes

* **deps:** bumps spigradle-annotations for fixing plugin-apt issue ([b19b4e5](https://github.com/spigradle/spigradle/commit/b19b4e564692572b0fbd532499a0e53910164ae7))

# [2.0.0](https://github.com/spigradle/spigradle/compare/v1.4.1...v2.0.0) (2020-07-03)


### Bug Fixes

* rename task 'spigotPluginYaml' to 'generateSpigotDescription' ([728ccf6](https://github.com/spigradle/spigradle/commit/728ccf62bcfef394b153662b19251018640ddafd))


### chore

* change annotations @Plugin, @PluginMain package ([aebfe46](https://github.com/spigradle/spigradle/commit/aebfe467d092ee3724849417007e5defcd2f096c))


### BREAKING CHANGES

* the annotations @Plugin and @PluginMain repackaged to `kr.entree.spigradle.annotations`

Signed-off-by: JunHyung Lim <entrypointkr@gmail.com>
* task 'spigotPluginYaml' renamed to 'GenerateSpigotDescription'. Sorry about the breaking change in 1.3.

Signed-off-by: JunHyung Lim <entrypointkr@gmail.com>

## [1.4.1](https://github.com/spigradle/spigradle/compare/v1.4.0...v1.4.1) (2020-07-02)

### Bug Fixes

* project artifact resolution for debug task ([821d975](https://github.com/spigradle/spigradle/commit/821d97559829bfb7487f4ebaad30e87fda1dd939))

# [1.4.0](https://github.com/spigradle/spigradle/compare/v1.3.1...v1.4.0) (2020-07-02)


### Features

* rollback breaking changes ([#17](https://github.com/spigradle/spigradle/issues/17)) ([b80868f](https://github.com/spigradle/spigradle/commit/b80868f30831ea46bbc4a4f8cb2c9690f3cc4b06))

## [~~1.3.1~~](https://github.com/spigradle/spigradle/compare/v1.3.1...v1.4.0) ~~(2020-06-28)~~

_NOTE: It has breaking changes. Use 2.x instead of this version._

Special thanks to contributors: @scpketer @portlek

### Bug Fixes

* Error when using commands and permissions
* @PluginMain not worked.

### Features

* Add groovy extension 'POSTWORLD'
* Add groovy DSL helpers for Command, Permission.

## [~~1.3.0~~](https://github.com/spigradle/spigradle/compare/v1.3.1...v1.4.0) ~~(2020-06-25)~~

_NOTE: It has breaking changes. Use 2.x instead of this version._

### Features

* Bungeecord plugin
* Nukkit plugin
* Debug tasks: BuildTools, running server, plugin dependency auto resolution...
* Support UP-TO-DATE check for every tasks
* Faster main class detection
* Support MockBukkit as default
* New repo/dep shortcuts: mockBukkit(), vaultAll()
* Added Download task.
* Generate Remote configuration for IntelliJ IDEA
