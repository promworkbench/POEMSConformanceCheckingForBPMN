package org.processmining.sccwbpmnnpos.models.bpmn.stochastic.execution.firingoptions;

import org.processmining.sccwbpmnnpos.models.bpmn.execution.firable.alternatives.MarkingBpmnNodeFiringOption;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.BpmnMarking;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.node.ExecutableBpmnNode;
import org.processmining.stochasticbpmn.models.stochastic.Probability;

public class StochasticMarkingBpmnNodeFiringOption extends MarkingBpmnNodeFiringOption implements StochasticBpmnNodeFiringOption {
    private final Probability probability;

    public StochasticMarkingBpmnNodeFiringOption(ExecutableBpmnNode node, BpmnMarking marking, Probability probability) {
        super(node, marking);
        this.probability = probability;
    }

    public Probability getProbability() {
        return probability;
    }
}