package kr.entree.spigradle

/**
 * The annotation used to specify the main class.
 */
@Target(AnnotationTarget.CLASS)
@Retention
annotation class PluginMain

typealias Plugin = PluginMain