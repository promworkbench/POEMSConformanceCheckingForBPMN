package org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.stochastic.statespace.language.path;

import org.processmining.models.graphbased.directed.transitionsystem.ReachabilityGraph;
import org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.statespace.path.BpmnPOReachabilityGraphPathConstructor;
import org.processmining.sccwbpmnnpos.algorithms.inputs.stochastic_language.stopping.StochasticLanguageGeneratorStopper;
import org.processmining.sccwbpmnnpos.algorithms.utils.stochastics.sampling.strategy.graph.StochasticGraphPathSamplingStrategy;
import org.processmining.sccwbpmnnpos.algorithms.utils.stochastics.sampling.strategy.graph.StochasticGraphPathSamplingStrategy.GraphSamplingType;
import org.processmining.sccwbpmnnpos.models.bpmn.stochastic.execution.node.factory.ExecutableStochasticBpmnNodeFactory;
import org.processmining.sccwbpmnnpos.models.bpmn.stochastic.language.path.BpmnStochasticPOPathLanguage;

public interface StochasticBpmnPORG2StochasticPathLanguageConverter {
    static StochasticBpmnPORG2StochasticPathLanguageConverter getInstance(GraphSamplingType type,
                                                                          StochasticLanguageGeneratorStopper stopper, int maxPathLength) {
        return getInstance(type, stopper, ExecutableStochasticBpmnNodeFactory.getInstance(), maxPathLength);
    }

    static StochasticBpmnPORG2StochasticPathLanguageConverter getInstance(GraphSamplingType type,
                                                                          StochasticLanguageGeneratorStopper stopper,
                                                                          ExecutableStochasticBpmnNodeFactory nodeFactory,  int maxPathLength) {
        BpmnPOReachabilityGraphPathConstructor pathConstructor = BpmnPOReachabilityGraphPathConstructor.getInstance(nodeFactory);
        return new StochasticBpmnPORG2StochasticPathLanguageConverterImpl(StochasticGraphPathSamplingStrategy.getInstance(type), stopper, pathConstructor, maxPathLength);
    }

    static StochasticBpmnPORG2StochasticPathLanguageConverter getInstance() {
        return getInstance(StochasticGraphPathSamplingStrategy.getDefaultType(),
                StochasticLanguageGeneratorStopper.getInstance(), 1000);
    }

    BpmnStochasticPOPathLanguage convert(ReachabilityGraph transitionSystem);
}
