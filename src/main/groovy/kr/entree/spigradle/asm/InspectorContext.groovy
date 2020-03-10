package kr.entree.spigradle.asm

/**
 * Created by JunHyung Lim on 2019-12-12
 */
class InspectorContext {
    String mainClass
    boolean pluginsAnnotationFound = false

    def isDone() {
        return mainClass != null
    }
}
