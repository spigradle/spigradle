package kr.entree.spigradle.module.common.task

import kr.entree.spigradle.internal.PLUGIN_APT_DEFAULT_PATH
import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.SkipWhenEmpty
import org.gradle.api.tasks.TaskAction
import org.gradle.work.Incremental
import org.gradle.work.InputChanges
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes
import java.io.File
import javax.inject.Inject

/**
 * Created by JunHyung Lim on 2020-05-20
 */
open class SubclassDetectionTask @Inject constructor(private val superClassName: String) : DefaultTask() {
    @get:Incremental
    @get:SkipWhenEmpty
    @get:InputFiles
    var classDirectories: FileCollection = project.files()

    @get:OutputFile
    var destination: File = File(project.buildDir, PLUGIN_APT_DEFAULT_PATH)

    @TaskAction
    fun inspect(inputChanges: InputChanges) {
        val context = SubclassDetectionContext().apply { superClasses += superClassName }
        val options = ClassReader.SKIP_CODE and ClassReader.SKIP_DEBUG and ClassReader.SKIP_FRAMES
        inputChanges.getFileChanges(classDirectories).asSequence().takeWhile {
            context.detectedMainClass == null
        }.map { it.file }.filter {
            it.extension == "class"
        }.forEach { classFile ->
            classFile.inputStream().buffered().use {
                ClassReader(it).accept(SubclassDetector(context), options)
            }
        }
        val detectedClass = context.detectedMainClass ?: return
        destination.apply {
            parentFile.mkdirs()
        }.writeText(detectedClass.replace('/', '.'))
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