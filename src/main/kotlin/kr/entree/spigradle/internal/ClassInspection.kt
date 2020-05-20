package kr.entree.spigradle.internal

import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.withConvention
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassReader.*
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes

/**
 * Created by JunHyung Lim on 2020-05-18
 */
internal fun Project.findProjectMainClass(superClassName: String): String? {
    val context = MainClassFinderContext().apply { superClasses += superClassName }
    val options = SKIP_CODE and SKIP_DEBUG and SKIP_FRAMES
    withConvention(JavaPluginConvention::class) {
        sourceSets["main"].output.classesDirs
    }.asSequence().flatMap { dir ->
        dir.walk().filter { it.extension == "class" }
    }.takeWhile {
        context.foundMainClass == null
    }.forEach { classFile ->
        classFile.inputStream().buffered().use {
            ClassReader(it).accept(MainClassFinder(context), options)
        }
    }
    return context.foundMainClass?.replace('/', '.')
}

class MainClassFinderContext {
    val superClasses = mutableSetOf<String>()
    var foundMainClass: String? = null
}

class MainClassFinder(private val context: MainClassFinderContext) : ClassVisitor(Opcodes.ASM8) {
    override fun visit(version: Int, access: Int, name: String, signature: String?, superName: String?, interfaces: Array<out String>?) {
        if (superName != null && superName in context.superClasses) {
            if (access.isAbstract) {
                context.superClasses += name
            } else if (access.isPublic) {
                context.foundMainClass = name
            }
        }
    }

    private val Int.isPublic get() = (this and Opcodes.ACC_PUBLIC) != 0
    private val Int.isAbstract get() = (this and Opcodes.ACC_ABSTRACT) != 0
}