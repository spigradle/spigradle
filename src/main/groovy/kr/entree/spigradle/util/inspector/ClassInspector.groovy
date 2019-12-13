package kr.entree.spigradle.util.inspector

import kr.entree.spigradle.util.InspectorResult
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes

/**
 * Created by JunHyung Lim on 2019-12-12
 */
class ClassInspector extends ClassVisitor {
    final InspectorResult result
    final Set<String> targets

    ClassInspector(int api, InspectorResult result, Set<String> targets) {
        super(api)
        this.result = result
        this.targets = targets
    }

    static def isPublic(int access) {
        return (access & Opcodes.ACC_PUBLIC) != 0
    }

    static def isNotAbstract(int access) {
        return (access & Opcodes.ACC_ABSTRACT) == 0
    }

    @Override
    void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        if (targets.contains(superName)) {
            if (isPublic(access) && isNotAbstract(access)) {
                result.mainClass = name.replace('/', '.')
            } else {
                targets.add(name)
            }
        }
    }
}
