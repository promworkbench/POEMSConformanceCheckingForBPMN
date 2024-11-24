package org.processmining.sccwbpmnnpos.models.bpmn.execution.firable.alternatives;

import org.processmining.models.graphbased.directed.bpmn.BPMNEdge;
import org.processmining.models.graphbased.directed.bpmn.BPMNNode;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.node.ExecutableBpmnNode;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.BpmnMarking;

import java.util.Collection;
import java.util.Objects;

public class MarkingBpmnNodeFiringOption implements BpmnNodeFiringOption {
    private final ExecutableBpmnNode node;
    private final BpmnMarking marking;

    public MarkingBpmnNodeFiringOption(ExecutableBpmnNode node, BpmnMarking marking) {
        this.node = node;
        this.marking = marking;
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
    public BpmnMarking getMarking() {
        return marking;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof MarkingBpmnNodeFiringOption)) return false;
        MarkingBpmnNodeFiringOption that = (MarkingBpmnNodeFiringOption) object;
        return Objects.equals(node, that.node) && Objects.equals(marking, that.marking);
    }

    @Override
    public int hashCode() {
        return Objects.hash(node, marking);
    }

    @Override
    public String toString() {
        return node.toString() + ": " + marking;
    }
}
