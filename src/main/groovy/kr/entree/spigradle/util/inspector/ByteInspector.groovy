package kr.entree.spigradle.util.inspector

import org.gradle.api.Project
import org.gradle.api.tasks.compile.AbstractCompile
import org.objectweb.asm.ClassReader
import org.objectweb.asm.Opcodes

import java.nio.file.Files

import static org.objectweb.asm.ClassReader.*

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
            Files.walk(directory.toPath()).withCloseable { subDirs ->
                subDirs.findAll {
                    it.toString().endsWith('.class')
                }.find { subDir ->
                    subDir.newInputStream().withCloseable { input ->
                        def reader = new ClassReader(input)
                        reader.accept(
                                new ClassInspector(Opcodes.ASM7, context, targets),
                                SKIP_CODE | SKIP_DEBUG | SKIP_FRAMES
                        )
                    }
                    context.isDone()
                }
            }
            context.isDone()
        }
        return context
    }
}