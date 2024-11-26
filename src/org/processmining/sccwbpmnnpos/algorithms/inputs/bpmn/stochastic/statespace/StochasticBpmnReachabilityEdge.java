package org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.stochastic.statespace;

import org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.statespace.BpmnReachabilityGraphEdge;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.path.BpmnPartiallyOrderedPath;
import org.processmining.stochasticbpmn.models.stochastic.Probability;
import org.processmining.stochasticbpmn.models.stochastic.StochasticObject;

public class StochasticBpmnReachabilityEdge extends BpmnReachabilityGraphEdge implements StochasticObject {
    private final Probability probability;

    public StochasticBpmnReachabilityEdge(BpmnPartiallyOrderedPath path, Probability probability) {
        super(path);
        this.probability = probability;
    }

    public Probability getProbability() {
        return probability;
    }

    @Override
    public String toString() {
        return getProbability() + ": " + super.toString();
    }
}
