package org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.stochastic.statespace;

import org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.statespace.BpmnReachabilityGraphEdge;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.path.BpmnExecutionPath;

public class StochasticBpmnReachabilityEdge extends BpmnReachabilityGraphEdge {
    private final double probability;

    public StochasticBpmnReachabilityEdge(BpmnExecutionPath path, double probability) {
        super(path);
        this.probability = probability;
    }

    public double getProbability() {
        return probability;
    }

    @Override
    public String toString() {
        return getProbability() + ": " + super.toString();
    }
}
