package kr.entree.spigradle.task

import kr.entree.spigradle.extension.PluginAttributes
import kr.entree.spigradle.util.inspector.ByteInspector
import kr.entree.spigradle.util.inspector.InspectorResult
import kr.entree.spigradle.util.mapper.ActualNames
import kr.entree.spigradle.util.mapper.Mapper
import kr.entree.spigradle.util.yaml.SpigradleRepresenter
import org.gradle.api.DefaultTask
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.jvm.tasks.Jar
import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.Yaml

import java.lang.reflect.Field
import java.lang.reflect.Modifier

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
        Object
        def attributes = [
                'main'   : requireNonNull(
                        attributes.main.getOrElse(inspected.mainClass),
                        'Spigradle couldn\'t find a main class automatically. ' +
                                'Please set a \'main\' property in spigot {} block in build.gradle.'
                ),
                'name'   : attributes.name.getOrElse(project.name),
                'version': attributes.version.getOrElse(project.version)
        ]
        PluginAttributes.declaredFields.grep { Field field ->
            !field.synthetic && !Modifier.isStatic(field.modifiers)
        }.each {
            it.setAccessible(true)
            def property = it.get(this.attributes)
            def value = null
            if (property instanceof NamedDomainObjectContainer) {
                def map = property.getAsMap()
                if (!map.isEmpty()) {
                    value = map
                }
            } else if (property instanceof Provider) {
                value = property.getOrNull()
            }
            if (checkValid(value)) {
                def key = ActualNames.get(it).replaceAll(
                        '([a-z])([A-Z]+)',
                        '$1-$2'
                ).toLowerCase()
                attributes[key] = value
            }
        }
        return Mapper.map(attributes)
    }

    static boolean checkValid(Object obj) {
        if (obj == null) {
            return false
        }
        if ((obj instanceof Map || obj instanceof Collection)
                && obj.isEmpty()) {
            return false
        }
        return true
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
