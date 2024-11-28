package org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.stochastic.statespace;

import org.apache.logging.log4j.core.config.LoggerConfig;
import org.processmining.models.graphbased.directed.transitionsystem.ReachabilityGraph;
import org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.statespace.Bpmn2POReachabilityGraphConverterImpl;
import org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.statespace.BpmnNoOptionToCompleteException;
import org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.statespace.BpmnReachabilityGraphEdge;
import org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.statespace.BpmnUnboundedException;
import org.processmining.sccwbpmnnpos.algorithms.utils.cartesianproduct.CartesianProductCalculator;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.firable.alternatives.BpmnNodeFiringOption;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.factory.BpmnMarkingFactory;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.path.BpmnPartiallyOrderedPath;
import org.processmining.sccwbpmnnpos.models.bpmn.stochastic.execution.firingoptions.StochasticBpmnNodeFiringOption;
import org.processmining.sccwbpmnnpos.models.bpmn.stochastic.execution.node.factory.ExecutableStochasticBpmnNodeFactory;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticBPMNDiagram;
import org.processmining.stochasticbpmn.models.stochastic.Probability;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.logging.Logger;

public class StochasticBpmn2POReachabilityGraphConverterImpl extends Bpmn2POReachabilityGraphConverterImpl implements StochasticBpmn2POReachabilityGraphConverter {
    public StochasticBpmn2POReachabilityGraphConverterImpl(ExecutableStochasticBpmnNodeFactory nodeFactory,
                                                           BpmnMarkingFactory markingFactory,
                                                           CartesianProductCalculator cartesianProductCalculator) {
        super(nodeFactory, markingFactory, cartesianProductCalculator);
    }

    @Override
    public ReachabilityGraph convert(StochasticBPMNDiagram bpmnDiagram) throws BpmnNoOptionToCompleteException,
            BpmnUnboundedException {
        return super.convert(bpmnDiagram);
    }

    @Override
    protected BpmnReachabilityGraphEdge getEdge(Collection<BpmnNodeFiringOption> firingOptions,
                                                BpmnPartiallyOrderedPath path) {
        Probability probability = Probability.ONE;
        for (BpmnNodeFiringOption firingOption : firingOptions) {
            if (firingOption instanceof StochasticBpmnNodeFiringOption) {
                probability = probability.multiply(((StochasticBpmnNodeFiringOption) firingOption).getProbability());
            }
        }
        return new StochasticBpmnReachabilityEdge(path, probability);
    }
}
