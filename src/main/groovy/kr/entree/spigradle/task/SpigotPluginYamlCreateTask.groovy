package kr.entree.spigradle.task

import kr.entree.spigradle.extension.PluginAttributes
import kr.entree.spigradle.util.inspector.ByteInspector
import kr.entree.spigradle.util.inspector.InspectorResult
import kr.entree.spigradle.util.mapper.Mapper
import kr.entree.spigradle.util.yaml.SpigradleRepresenter
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.jvm.tasks.Jar
import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.Yaml

import static java.util.Objects.requireNonNull

/**
 * Created by JunHyung Lim on 2019-12-12
 */
class SpigotPluginYamlCreateTask extends DefaultTask {
    @Input
    PluginAttributes attributes
    @Input
    String encoding = 'UTF-8'
    @Input
    Yaml yaml = createYaml()

    @TaskAction
    def createPluginYaml() {
        def file = new File(temporaryDir, 'plugin.yml')
        file.newWriter(encoding).withCloseable {
            writePluginYaml(it)
        }
        project.tasks.withType(Jar) {
            it.from file
        }
    }

    def writePluginYaml(Writer writer) {
        def inspected = new ByteInspector(project).doInspect()
        yaml.dump(createMap(inspected), writer)
    }

    def createMap(InspectorResult inspected) {
        attributes.with {
            main = main ?: inspected.mainClass
            name = name ?: project.name
            version = version ?: project.version
        }
        def yamlMap = Mapper.mapping(attributes, true) as Map<String, Object>
        if (yamlMap.main == null) {
            throw new IllegalArgumentException(
                    """\
                        Spigradle couldn\'t find a main class automatically.
                        Please set a 'main' property in spigot {} block in build.gradle\
                    """.stripIndent()
            )
        }
        requireNonNull(
                yamlMap.main,
        )
        return yamlMap
    }

    static Yaml createYaml() {
        def options = new DumperOptions()
        options.with {
            defaultFlowStyle = DumperOptions.FlowStyle.BLOCK
            indicatorIndent = indent - 1
        }
        return new Yaml(new SpigradleRepresenter(), options)
    }
}
