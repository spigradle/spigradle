package kr.entree.spigradle.internal

/**
 * Created by JunHyung Lim on 2020-05-18
 */
internal object Messages {
    fun noMainFound(extensionName: String, taskName: String) = """
        Spigradle couldn't find main class automatically!
        Please present your main class using the annotation @kr.entree.spigradle.PluginMain or @Plugin,
        or set the 'main' property in $extensionName {} block on build.gradle,
        or just disable $taskName task: 'tasks.$taskName.enabled = false'
    """.trimIndent()
}