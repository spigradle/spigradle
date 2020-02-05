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
    static def SPIGOT_PLUGIN_NAME = 'org/bukkit/plugin/java/JavaPlugin'
    final Collection<File> directories

    ByteInspector(directories) {
        this.directories = directories
    }

    ByteInspector(Project project) {
        this(project.tasks.findAll { it instanceof AbstractCompile }
                .collect { (it as AbstractCompile).destinationDir }
                .findAll { it.isDirectory() })
    }

    InspectorResult doInspect() {
        def result = new InspectorResult()
        def targets = new HashSet([SPIGOT_PLUGIN_NAME])
        directories.find { directory ->
            Files.walk(directory.toPath()).withCloseable { walk ->
                walk.findAll {
                    it.toString().endsWith('.class')
                }.find {
                    it.newInputStream().withCloseable { input ->
                        def reader = new ClassReader(input)
                        reader.accept(
                                new ClassInspector(Opcodes.ASM7, result, targets),
                                SKIP_CODE | SKIP_DEBUG | SKIP_FRAMES
                        )
                    }
                    return result.isDone()
                }
            }
            return result.isDone()
        }
        return result
    }
}