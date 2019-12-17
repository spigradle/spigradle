package kr.entree.spigradle.util.inspector

/**
 * Created by JunHyung Lim on 2019-12-12
 */
class InspectorResult {
    String mainClass

    def isDone() {
        return mainClass != null
    }
}
