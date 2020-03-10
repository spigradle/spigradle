package kr.entree.spigradle.asm

import kr.entree.spigradle.asm.visitor.ClassInspector
import kr.entree.spigradle.asm.visitor.ClassOptimizer
import org.gradle.api.Project
import org.gradle.api.tasks.compile.AbstractCompile
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes

import java.nio.file.Files
import java.nio.file.Path

import static org.objectweb.asm.ClassReader.*
import static org.objectweb.asm.ClassWriter.COMPUTE_FRAMES

/**
 * Created by JunHyung Lim on 2019-12-12
 */
class ByteInspector {
    public static final def SPIGOT_PLUGIN_NAME = 'org/bukkit/plugin/java/JavaPlugin'
    public static final def PLUGIN_ANNOTATION_NAME = 'Lkr/entree/spigradle/annotation/Plugin;'
    final Collection<File> directories

    ByteInspector(directories) {
        this.directories = directories
    }

    ByteInspector(Project project) {
        this(project.tasks.findAll { it instanceof AbstractCompile }
                .collect { (it as AbstractCompile).destinationDir }
                .findAll { it.isDirectory() })
    }

    InspectorContext doInspect() {
        def context = new InspectorContext()
        def targets = new HashSet([SPIGOT_PLUGIN_NAME])
        directories.find { directory ->
            Files.walk(directory.toPath()).withCloseable { classPaths ->
                classPaths.findAll {
                    it.toString().endsWith('.class')
                }.<Path> find { classPath ->
                    classPath.withInputStream { input ->
                        def classBytes = input.bytes
                        def reader = new ClassReader(classBytes)
                        reader.accept(
                                new ClassInspector(Opcodes.ASM7, context, targets),
                                SKIP_CODE | SKIP_DEBUG | SKIP_FRAMES
                        )
                        if (context.pluginsAnnotationFound) {
                            redefineCompileThings(classBytes, classPath)
                            context.pluginsAnnotationFound = false
                        }
                    }
                    context.isDone()
                }
            }
            context.isDone()
        }
        return context
    }

    private static void redefineCompileThings(byte[] classBytes, Path classPath) {
        def newClassBytes = removeCompileThings(classBytes)
        if (Arrays.equals(classBytes, newClassBytes)) {
            return
        }
        classPath.withOutputStream { output ->
            output.write(newClassBytes)
        }
    }

    private static byte[] removeCompileThings(byte[] classBytes) {
        def reader = new ClassReader(classBytes)
        def writer = new ClassWriter(COMPUTE_FRAMES)
        reader.accept(
                new ClassOptimizer(Opcodes.ASM7, writer),
                EXPAND_FRAMES
        )
        return writer.toByteArray()
    }
}