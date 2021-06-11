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

package kr.entree.spigradle.module.common

import kr.entree.spigradle.annotations.PluginType
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.provider.Property
import org.gradle.api.tasks.*
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.kotlin.dsl.*
import org.gradle.work.InputChanges
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes
import java.io.File

/**
 * Finds the main class that extends the given super-class.
 *
 * Groovy Example:
 *
 * ```groovy
 * import kr.entree.spigradle.module.common.SubclassDetection
 *
 * task findSubclass(type: SubclassDetection) {
 *   superClassName = 'com.my.sample.SuperType'
 *   classDirectories.from sourceSets.main.output.classesDirs
 *   outputFile = file('result.txt')
 * }
 * ```
 *
 * ```kotiln
 * import kr.entree.spigradle.module.common.SubclassDetection
 *
 * tasks {
 *   val findSubclass(type: SubclassDetection) {
 *     superClassName.set("com.my.sample.SuperType")
 *     classDirectories.from(sourceSets["main"].output.classesDirs)
 *     outputFile.set(file("result.txt"))
 *   }
 * }
 * ```
 *
 * @since 1.3.0
 */
@Suppress("UnstableApiUsage")
open class SubclassDetection : DefaultTask() {
    init {
        group = "spigradle"
        description = "Detect the jvm subclass."
    }

    /**
     * The name of super-class used to detect a sub-class.
     */
    @get:Input
    val superClassName: Property<String> = project.objects.property()

    /**
     * The class directories used to target of the sub-class detection.
     */
    @get:SkipWhenEmpty
    @get:InputFiles
    val classDirectories: ConfigurableFileCollection = project.objects.fileCollection()

    /**
     * The path of plain text includes the detection result.
     */
    @get:OutputFile
    val outputFile = project.objects.property<File>()

    @TaskAction
    fun inspect(inputChanges: InputChanges) {
        val context = SubclassDetectionContext().apply { superClasses += superClassName.get() }
        val options = ClassReader.SKIP_CODE and ClassReader.SKIP_DEBUG and ClassReader.SKIP_FRAMES
        inputChanges.getFileChanges(classDirectories).asSequence().takeWhile {
            context.detectedMainClass == null
        }.map { it.file }.filter {
            it.extension == "class" && it.isFile
        }.forEach { classFile ->
            classFile.inputStream().buffered().use {
                ClassReader(it).accept(SubclassDetector(context), options)
            }
        }
        val detectedClass = context.detectedMainClass ?: return
        outputFile.get().apply {
            parentFile.mkdirs()
        }.writeText(detectedClass.replace('/', '.'))
    }

    companion object {
        fun register(project: Project, taskName: String, type: PluginType): TaskProvider<SubclassDetection> {
            val sourceSets = project.withConvention(JavaPluginConvention::class) { sourceSets }
            return project.tasks.register(taskName, SubclassDetection::class) {
                val pathFile = project.getPluginMainPathFile(type)
                val compileJava = project.tasks.named<JavaCompile>("compileJava")
                dependsOn(compileJava)
                /*
                NOTE:
                If put a FileCollection into the `from` makes this task ordered after `classes`.
                Therefore put List<File> instead.
                 */
                classDirectories.from(sourceSets["main"].output.classesDirs.files)
                outputFile.convention(pathFile) // defaults to pathFile
            }
        }
    }
}

class SubclassDetectionContext {
    val superClasses = mutableSetOf<String>()
    var detectedMainClass: String? = null
}

class SubclassDetector(private val context: SubclassDetectionContext) : ClassVisitor(Opcodes.ASM8) {
    override fun visit(version: Int, access: Int, name: String, signature: String?, superName: String?, interfaces: Array<out String>?) {
        if (superName != null && superName in context.superClasses) {
            if (access.isAbstract) {
                context.superClasses += name
            } else if (access.isPublic) {
                context.detectedMainClass = name
            }
        }
    }

    private val Int.isPublic get() = (this and Opcodes.ACC_PUBLIC) != 0
    private val Int.isAbstract get() = (this and Opcodes.ACC_ABSTRACT) != 0
}