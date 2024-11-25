package org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.stochastic.statespace;

import org.processmining.models.graphbased.directed.transitionsystem.TransitionSystem;
import org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.statespace.Bpmn2PartiallyOrderedReachabilityGraphConverterImpl;
import org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.statespace.BpmnNoOptionToCompleteException;
import org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.statespace.BpmnReachabilityGraphEdge;
import org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.statespace.BpmnUnboundedException;
import org.processmining.sccwbpmnnpos.algorithms.utils.cartesianproduct.CartesianProductCalculator;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.firable.alternatives.BpmnNodeFiringOption;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.factory.BpmnMarkingFactory;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.node.factory.ExecutableBpmnNodeFactory;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.path.BpmnExecutionPath;
import org.processmining.sccwbpmnnpos.models.bpmn.stochastic.execution.firingoptions.StochasticBpmnNodeFiringOption;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticBPMNDiagram;

import java.util.Collection;

public class StochasticBpmn2PartiallyOrderedReachabilityGraphConverter extends Bpmn2PartiallyOrderedReachabilityGraphConverterImpl implements StochasticBpmn2ReachabilityGraphConverter {
    public StochasticBpmn2PartiallyOrderedReachabilityGraphConverter(ExecutableBpmnNodeFactory nodeFactory,
                                                                     BpmnMarkingFactory markingFactory,
                                                                     CartesianProductCalculator cartesianProductCalculator) {
        super(nodeFactory, markingFactory, cartesianProductCalculator);
    }

    @Override
    public TransitionSystem convert(StochasticBPMNDiagram bpmnDiagram) throws BpmnNoOptionToCompleteException,
            BpmnUnboundedException {
        return super.convert(bpmnDiagram);
    }

    @Override
    protected BpmnReachabilityGraphEdge getEdge(Collection<BpmnNodeFiringOption> firingOptions,
                                                BpmnExecutionPath path) {
        double probability = 1.0;
        for (BpmnNodeFiringOption firingOption : firingOptions) {
            if (firingOption instanceof StochasticBpmnNodeFiringOption) {
                probability *= ((StochasticBpmnNodeFiringOption) firingOption).getProbability();
            }
        }
        return new StochasticBpmnReachabilityEdge(path, probability);
    }
}
