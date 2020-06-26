/*
 * Copyright (c) 2020 Spigradle contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kr.entree.spigradle.internal

import com.google.auto.service.AutoService
import kr.entree.spigradle.PluginMain
import java.io.File
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.QualifiedNameable
import javax.lang.model.element.TypeElement

/**
 * Created by JunHyung Lim on 2020-05-18
 */
const val PLUGIN_APT_RESULT_PATH_KEY = "pluginAnnotationProcessResultPath"
const val PLUGIN_APT_DEFAULT_PATH = "spigradle/plugin-main"

@SupportedAnnotationTypes("kr.entree.spigradle.PluginMain")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedOptions(PLUGIN_APT_RESULT_PATH_KEY)
@AutoService(Processor::class)
internal class PluginAnnotationProcessor : AbstractProcessor() {
    var pluginName = ""

    override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {
        if (roundEnv.processingOver() && pluginName.isNotBlank()) {
            File(processingEnv.options[PLUGIN_APT_RESULT_PATH_KEY] ?: "build/$PLUGIN_APT_DEFAULT_PATH").apply {
                parentFile.mkdirs()
                writeText(pluginName)
            }
        } else {
            val pluginAnnotation = PluginMain::class.java
            pluginName = roundEnv.getElementsAnnotatedWith(pluginAnnotation)
                    .filterIsInstance<QualifiedNameable>()
                    .find {
                        it.getAnnotation(pluginAnnotation) != null
                    }?.qualifiedName?.toString() ?: ""
        }
        return true
    }
}