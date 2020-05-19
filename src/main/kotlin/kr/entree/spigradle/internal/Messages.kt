package kr.entree.spigradle.internal

/**
 * Created by JunHyung Lim on 2020-05-18
 */
internal object Messages {
    fun noMainFound(pluginName: String, taskName: String) = """
        Spigradle couldn't find main class automatically!
        Please present your main class using @kr.entree.spigradle.Plugin annotation,
        or set the 'main' property in $pluginName {} block on build.gradle,
        or just disable $taskName task: 'tasks.$taskName.enabled = false'
    """.trimIndent()
}