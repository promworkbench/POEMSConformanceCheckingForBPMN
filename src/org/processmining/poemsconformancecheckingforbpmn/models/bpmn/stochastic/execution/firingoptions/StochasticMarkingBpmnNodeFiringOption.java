package org.processmining.poemsconformancecheckingforbpmn.models.bpmn.stochastic.execution.firingoptions;

import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.firable.alternatives.MarkingBpmnNodeFiringOption;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.marking.BpmnMarking;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.node.ExecutableBpmnNode;
import org.processmining.stochasticbpmn.models.stochastic.Probability;

public class StochasticMarkingBpmnNodeFiringOption extends MarkingBpmnNodeFiringOption implements StochasticBpmnNodeFiringOption {
    private final Probability probability;

    public StochasticMarkingBpmnNodeFiringOption(ExecutableBpmnNode node, BpmnMarking consumeMarking,
                                                 BpmnMarking produceMarking,
                                                 Probability probability) {
        super(node, consumeMarking, produceMarking);
        this.probability = probability;
    }

    public Probability getProbability() {
        return probability;
    }
}
