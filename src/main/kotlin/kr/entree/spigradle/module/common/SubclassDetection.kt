package kr.entree.spigradle.module.common

import kr.entree.spigradle.internal.PLUGIN_APT_DEFAULT_PATH
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileCollection
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.provider.Property
import org.gradle.api.tasks.*
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.property
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withConvention
import org.gradle.work.InputChanges
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes
import java.io.File

/**
 * Created by JunHyung Lim on 2020-05-20
 */
@Suppress("UnstableApiUsage")
open class SubclassDetection : DefaultTask() {
    init {
        group = "spigradle"
        description = "Detect the jvm subclass."
    }

    @get:Input
    val superClassName: Property<String> = project.objects.property()

    @get:SkipWhenEmpty
    @get:InputFiles
    val classDirectories: ConfigurableFileCollection = project.objects.fileCollection()

    @get:OutputFile
    val outputFile = project.objects.property<File>()
            .convention(File(project.buildDir, PLUGIN_APT_DEFAULT_PATH))

    @TaskAction
    fun inspect(inputChanges: InputChanges) {
        val context = SubclassDetectionContext().apply { superClasses += superClassName.get() }
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
        outputFile.get().apply {
            parentFile.mkdirs()
        }.writeText(detectedClass.replace('/', '.'))
    }

    companion object {
        fun register(project: Project, taskName: String, superName: String): TaskProvider<SubclassDetection> {
            val sourceSets = project.withConvention(JavaPluginConvention::class) { sourceSets }
            return project.tasks.register(taskName, SubclassDetection::class) {
                superClassName.set(superName)
                classDirectories.from(sourceSets["main"].output.classesDirs)
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