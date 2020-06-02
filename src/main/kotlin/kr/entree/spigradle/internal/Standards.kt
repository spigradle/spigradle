package kr.entree.spigradle.internal

import com.fasterxml.jackson.module.kotlin.readValue
import kr.entree.spigradle.module.common.SpigradlePlugin
import org.gradle.kotlin.dsl.support.useToRun
import java.io.File
import java.util.jar.JarFile

/**
 * Created by JunHyung Lim on 2020-06-02
 */
fun File.readPluginJar() = runCatching {
    JarFile(this).run {
        getEntry("plugin.yml")?.run {
            getInputStream(this)
        }
    }?.useToRun {
        SpigradlePlugin.yaml.readValue<Map<String, Any>>(this)
    }
}.getOrNull() ?: emptyMap()