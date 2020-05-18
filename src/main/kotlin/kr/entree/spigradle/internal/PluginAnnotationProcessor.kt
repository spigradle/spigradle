package kr.entree.spigradle.internal

import com.google.auto.service.AutoService
import kr.entree.spigradle.Plugin
import java.io.File
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.QualifiedNameable
import javax.lang.model.element.TypeElement

/**
 * Created by JunHyung Lim on 2020-05-18
 */
const val PLUGIN_APT_RESULT_PATH_KEY = "pluginAnnotationProcessResultPath"
const val PLUGIN_APT_DEFAULT_PATH = "spigradle/result.txt"

@SupportedAnnotationTypes("kr.entree.spigradle.Plugin")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedOptions(PLUGIN_APT_RESULT_PATH_KEY)
@AutoService(Processor::class)
internal class PluginAnnotationProcessor : AbstractProcessor() {
    var pluginName = ""

    override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {
        if (roundEnv.processingOver() && pluginName.isNotBlank()) {
            File(processingEnv.options[PLUGIN_APT_RESULT_PATH_KEY] ?: "build/$PLUGIN_APT_DEFAULT_PATH").apply {
                parentFile.mkdirs()
                bufferedWriter().use {
                    it.write(pluginName)
                }
            }
        } else {
            val pluginAnnotation = Plugin::class.java
            pluginName = roundEnv.getElementsAnnotatedWith(pluginAnnotation)
                    .filterIsInstance<QualifiedNameable>()
                    .find {
                        it.getAnnotation(pluginAnnotation) != null
                    }?.qualifiedName?.toString() ?: ""
        }
        return true
    }
}