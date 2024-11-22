package org.processmining.sccwbpmnnpos.models.bpmn.execution.node.factory;

import org.processmining.models.graphbased.NodeID;
import org.processmining.models.graphbased.directed.bpmn.BPMNNode;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.node.ExecutableBpmnNode;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CachedExecutableBpmnNodeFactory implements ExecutableBpmnNodeFactory {
    private final Map<NodeID, ExecutableBpmnNode> cache;
    private final ExecutableBpmnNodeFactory factory;

    public CachedExecutableBpmnNodeFactory(final ExecutableBpmnNodeFactory factory) {
        assert Objects.nonNull(factory);
        this.factory = factory;
        this.cache = new HashMap<>();
    }

    @Override
    public ExecutableBpmnNode create(BPMNNode node) {
        ExecutableBpmnNode firableNode = cache.get(node.getId());
        if (Objects.nonNull(firableNode)) return firableNode;
        ExecutableBpmnNode newNode = factory.create(node);
        cache.put(node.getId(), newNode);
        return newNode;
    }
}
