package kr.entree.spigradle.util

/**
 * Created by JunHyung Lim on 2019-12-12
 */
class InspectorResult {
    String mainClass

    def isDone() {
        return mainClass != null
    }
}
