package kr.entree.spigradle.util.inspector

/**
 * Created by JunHyung Lim on 2019-12-12
 */
class InspectorResult {
    String mainClass

    String getMainOrThrow() {
        if (mainClass == null) {
            throw new RuntimeException(
                    'Spigradle couldn\'t find a main class automatically. ' +
                    'Please set a \'main\' property in spigot {} block.'
            )
        }
        return mainClass
    }

    def isDone() {
        return mainClass != null
    }
}
