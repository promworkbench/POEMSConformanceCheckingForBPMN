package org.processmining.sccwbpmnnpos.models.bpmn.execution.firable.alternatives;

import org.processmining.sccwbpmnnpos.models.bpmn.execution.node.ExecutableBpmnNode;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.BpmnMarking;

import java.util.Objects;

public class MarkingBpmnNodeFiringOption implements BpmnNodeFiringOption {
    private final ExecutableBpmnNode node;
    private final BpmnMarking producesMarking;
    private final BpmnMarking consumesMarking;

    public MarkingBpmnNodeFiringOption(ExecutableBpmnNode node, BpmnMarking consumesMarking,
                                       BpmnMarking producesMarking) {
        this.node = node;
        this.producesMarking = producesMarking;
        this.consumesMarking = consumesMarking;
    }

    @Override
    public ExecutableBpmnNode getNode() {
        return node;
    }

    @Override
    public String getLabel() {
        return node.getNode().getLabel();
    }

    @Override
    public BpmnMarking getProducesMarking() {
        return producesMarking;
    }

    @Override
    public BpmnMarking getConsumesMarking() {
        return consumesMarking;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof MarkingBpmnNodeFiringOption)) return false;
        MarkingBpmnNodeFiringOption that = (MarkingBpmnNodeFiringOption) object;
        return Objects.equals(node, that.node) && Objects.equals(producesMarking, that.producesMarking);
    }

    @Override
    public int hashCode() {
        return Objects.hash(node, producesMarking);
    }

    @Override
    public String toString() {
        return node.toString() + ": " + producesMarking;
    }
}
