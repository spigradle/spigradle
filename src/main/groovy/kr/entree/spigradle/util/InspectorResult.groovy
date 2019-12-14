package kr.entree.spigradle.util

/**
 * Created by JunHyung Lim on 2019-12-12
 */
class InspectorResult {
    String mainClass

    String getMainOrThrow() {
        if (mainClass == null) {
            throw new RuntimeException('Spigradle couldn\'t find a main class. Please specify manually in spigot block.')
        }
        return mainClass
    }

    def isDone() {
        return mainClass != null
    }
}
