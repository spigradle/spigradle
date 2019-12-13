package kr.entree.spigradle.task

import kr.entree.spigradle.extension.PluginAttributes
import kr.entree.spigradle.util.ByteInspector
import kr.entree.spigradle.util.InspectorResult
import kr.entree.spigradle.util.Mapper
import org.gradle.api.DefaultTask
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.api.internal.file.collections.FileTreeAdapter
import org.gradle.api.internal.file.collections.GeneratedSingletonFileTree
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.jvm.tasks.Jar
import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.Yaml

/**
 * Created by JunHyung Lim on 2019-12-12
 */
class PluginYamlGenerater extends DefaultTask {
    @Input
    PluginAttributes attr
    @Input
    String encoding = 'UTF-8'

    def createFileTree(name, writer) {
        return new FileTreeAdapter(new GeneratedSingletonFileTree(
                temporaryDirFactory, name, writer
        ))
    }

    def createMap(InspectorResult inspected) {
        def attributes = [
                'main'   : attr.main.getOrElse(inspected.mainClass),
                'name'   : attr.name.getOrElse(project.name),
                'version': attr.version.getOrElse(project.version)
        ]
        PluginAttributes.declaredFields.grep {
            !it.synthetic
        }.each {
            it.setAccessible(true)
            def optional = it.get(attr)
            def value = null
            if (optional instanceof Property) {
                value = optional.getOrNull()
            } else if (optional instanceof NamedDomainObjectContainer) {
                def map = optional.getAsMap()
                if (!map.isEmpty()) {
                    value = map
                }
            }
            if (value != null) {
                def key = it.name.toLowerCase()
                attributes[key] = value
            }
        }
        return Mapper.map(attributes)
    }

    static Yaml createYaml() {
        def options = new DumperOptions()
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK)
        options.setPrettyFlow(true)
        return new Yaml(options)
    }

    def writePluginYaml(writer) {
        def inspected = inspectClasses(project)
        createYaml().dump(createMap(inspected), writer)
    }

    private static InspectorResult inspectClasses(Project project) {
        new ByteInspector(project).inspect()
    }

    @TaskAction
    def createPluginYaml() {
        def writer = new StringWriter()
        writePluginYaml(writer)
        project.tasks.findAll {
            it instanceof Jar
        } each {
            it.from createFileTree('plugin.yml') {
                it.write(writer.toString().getBytes(encoding))
            }
        }
    }
}
