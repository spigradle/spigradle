package kr.entree.spigradle.asm.visitor

import kr.entree.spigradle.asm.ByteInspector
import kr.entree.spigradle.asm.InspectorContext
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes

/**
 * Created by JunHyung Lim on 2019-12-12
 */
class ClassInspector extends ClassVisitor {
    final InspectorContext context
    final Set<String> targets
    String lastClassName = ''

    ClassInspector(int api, InspectorContext context, Set<String> targets) {
        super(api)
        this.context = context
        this.targets = targets
    }

    @Override
    void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        def formattedName = name.replace('/', '.')
        lastClassName = formattedName
        if (targets.contains(superName)) {
            if (isPublic(access) && isNotAbstract(access)) {
                context.mainClass = formattedName
            } else {
                targets.add(name)
            }
        }
    }

    @Override
    AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        if (!lastClassName.isEmpty() && descriptor == ByteInspector.PLUGIN_ANNOTATION_NAME) {
            context.mainClass = lastClassName
            context.pluginsAnnotationFound = true
        }
        return null
    }

    static def isPublic(int access) {
        return (access & Opcodes.ACC_PUBLIC) != 0
    }

    static def isNotAbstract(int access) {
        return (access & Opcodes.ACC_ABSTRACT) == 0
    }
}
