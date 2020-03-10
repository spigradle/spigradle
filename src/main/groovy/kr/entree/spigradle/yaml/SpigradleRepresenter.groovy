package kr.entree.spigradle.yaml

import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.nodes.Node
import org.yaml.snakeyaml.nodes.Tag
import org.yaml.snakeyaml.representer.Representer

/**
 * Created by JunHyung Lim on 2020-02-09
 */
class SpigradleRepresenter extends Representer {
    @Override
    protected Node representSequence(Tag tag, Iterable<?> sequence, DumperOptions.FlowStyle flowStyle) {
        return super.representSequence(tag, sequence, DumperOptions.FlowStyle.FLOW)
    }
}
