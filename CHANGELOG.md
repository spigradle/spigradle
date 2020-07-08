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