package org.processmining.sccwbpmnnpos.models.bpmn.execution.firable.alternatives;

import org.processmining.models.graphbased.directed.bpmn.BPMNEdge;
import org.processmining.models.graphbased.directed.bpmn.BPMNNode;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.node.ExecutableBpmnNode;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.BpmnMarking;

import java.util.Collection;

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
}
