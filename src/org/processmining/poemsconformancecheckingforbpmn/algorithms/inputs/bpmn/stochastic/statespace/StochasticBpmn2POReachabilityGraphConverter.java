package org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.bpmn.stochastic.statespace;

import org.processmining.models.graphbased.directed.transitionsystem.ReachabilityGraph;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.bpmn.statespace.BpmnNoOptionToCompleteException;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.bpmn.statespace.BpmnUnboundedException;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.utils.cartesianproduct.CartesianProductCalculator;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.marking.utils.BpmnMarkingUtils;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.stochastic.execution.node.factory.ExecutableStochasticBpmnNodeFactory;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticBPMNDiagram;

public interface StochasticBpmn2POReachabilityGraphConverter {
    static StochasticBpmn2POReachabilityGraphConverter getInstance() {
        ExecutableStochasticBpmnNodeFactory nodeFactory = ExecutableStochasticBpmnNodeFactory.getInstance();
        return getInstance(nodeFactory);
    }

    static StochasticBpmn2POReachabilityGraphConverter getInstance(ExecutableStochasticBpmnNodeFactory nodeFactory) {
        return new StochasticBpmn2POReachabilityGraphConverterImpl(nodeFactory, BpmnMarkingUtils.getInstance(),
                CartesianProductCalculator.getInstance());
    }

    ReachabilityGraph convert(StochasticBPMNDiagram bpmnDiagram) throws BpmnNoOptionToCompleteException,
            BpmnUnboundedException;
}
