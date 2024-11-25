package org.processmining.sccwbpmnnpos.models.bpmn.stochastic.execution.firingoptions;

import org.processmining.sccwbpmnnpos.models.bpmn.execution.firable.alternatives.MarkingBpmnNodeFiringOption;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.BpmnMarking;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.node.ExecutableBpmnNode;

public class StochasticMarkingBpmnNodeFiringOption extends MarkingBpmnNodeFiringOption implements StochasticBpmnNodeFiringOption {
    private final double probability;

    public StochasticMarkingBpmnNodeFiringOption(ExecutableBpmnNode node, BpmnMarking marking, double probability) {
        super(node, marking);
        this.probability = probability;
    }

    public double getProbability() {
        return probability;
    }
}
