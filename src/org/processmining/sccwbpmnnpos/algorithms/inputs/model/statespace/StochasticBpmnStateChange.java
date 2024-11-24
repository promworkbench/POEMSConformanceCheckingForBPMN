package org.processmining.sccwbpmnnpos.algorithms.inputs.model.statespace;

import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.BpmnMarking;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.path.BpmnExecutionPath;

import java.util.Objects;

public class StochasticBpmnStateChange {
    private final BpmnMarking targetMarking;
    private final BpmnExecutionPath path;
    private final Double weight;

    public StochasticBpmnStateChange(BpmnMarking targetMarking, BpmnExecutionPath path, Double weight) {
        this.targetMarking = targetMarking;
        this.path = path;
        this.weight = weight;
    }

    public BpmnMarking getTargetMarking() {
        return targetMarking;
    }

    public BpmnExecutionPath getPath() {
        return path;
    }

    public Double getWeight() {
        return weight;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof StochasticBpmnStateChange)) return false;
        StochasticBpmnStateChange that = (StochasticBpmnStateChange) object;
        return Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(path);
    }

    @Override
    public String toString() {
        return weight + ": " + path;
    }
}
