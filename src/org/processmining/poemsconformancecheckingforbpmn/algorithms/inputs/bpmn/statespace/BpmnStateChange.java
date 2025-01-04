package org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.bpmn.statespace;

import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.marking.BpmnMarking;

import java.util.Objects;

public class BpmnStateChange {
    private final BpmnMarking targetMarking;
    private final BpmnReachabilityGraphEdge edge;
    private final boolean complete;

    public BpmnStateChange(BpmnMarking targetMarking, BpmnReachabilityGraphEdge edge) {
        this(targetMarking, edge, true);
    }

    public BpmnStateChange(BpmnMarking targetMarking, BpmnReachabilityGraphEdge edge, boolean complete) {
        this.targetMarking = targetMarking;
        this.edge = edge;
        this.complete = complete;
    }

    public BpmnMarking getTargetMarking() {
        return targetMarking;
    }

    public BpmnReachabilityGraphEdge getEdge() {
        return edge;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof BpmnStateChange)) return false;
        BpmnStateChange that = (BpmnStateChange) object;
        return Objects.equals(edge, that.edge);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(edge);
    }

    @Override
    public String toString() {
        return edge.toString();
    }

    public boolean isComplete() {
        return complete;
    }
}
