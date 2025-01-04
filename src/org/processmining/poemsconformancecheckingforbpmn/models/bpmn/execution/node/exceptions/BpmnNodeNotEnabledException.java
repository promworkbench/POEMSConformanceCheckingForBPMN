package org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.node.exceptions;

import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.node.ExecutableBpmnNode;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.marking.BpmnMarking;

public class BpmnNodeNotEnabledException extends Exception {
    private final BpmnMarking marking;
    private final ExecutableBpmnNode node;


    public BpmnNodeNotEnabledException(BpmnMarking marking, ExecutableBpmnNode bpmnNode) {
        this.marking = marking;
        this.node = bpmnNode;
    }

    public BpmnMarking getMarking() {
        return marking;
    }

    public ExecutableBpmnNode getNode() {
        return node;
    }
}
