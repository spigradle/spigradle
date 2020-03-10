package kr.entree.spigradle.asm.visitor

import kr.entree.spigradle.asm.ByteInspector
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassVisitor

/**
 * Created by JunHyung Lim on 2020-03-10
 */
class ClassOptimizer extends ClassVisitor {
    ClassOptimizer(int api, ClassVisitor classVisitor) {
        super(api, classVisitor)
    }

    @Override
    AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        if (descriptor == ByteInspector.PLUGIN_ANNOTATION_NAME) {
            return null // Removing compile time annotation.
        }
        cv.visitAnnotation(descriptor, visible)
    }
}
