package org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.stochastic.statespace.language.path;

import org.processmining.models.graphbased.directed.transitionsystem.ReachabilityGraph;
import org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.statespace.path.BpmnPOReachabilityGraphPathConstructor;
import org.processmining.sccwbpmnnpos.algorithms.utils.stochastics.sampling.stopping.SamplingStoppingCriterion;
import org.processmining.sccwbpmnnpos.algorithms.utils.stochastics.sampling.strategy.graph.TransitionSamplingStrategy;
import org.processmining.sccwbpmnnpos.algorithms.utils.stochastics.sampling.strategy.graph.TansitionSamplingStrategyType;
import org.processmining.sccwbpmnnpos.models.bpmn.stochastic.execution.node.factory.ExecutableStochasticBpmnNodeFactory;
import org.processmining.sccwbpmnnpos.models.bpmn.stochastic.language.path.BpmnStochasticPOPathLanguage;

public interface StochasticBpmnPORG2StochasticPathLanguageConverter {
    static StochasticBpmnPORG2StochasticPathLanguageConverter getInstance(
            TansitionSamplingStrategyType type,
            SamplingStoppingCriterion stopper, int maxPathLength) {
        return getInstance(type, stopper, ExecutableStochasticBpmnNodeFactory.getInstance(), maxPathLength);
    }

    static StochasticBpmnPORG2StochasticPathLanguageConverter getInstance(
            TansitionSamplingStrategyType type,
            SamplingStoppingCriterion stopper,
            ExecutableStochasticBpmnNodeFactory nodeFactory, int maxPathLength) {
        BpmnPOReachabilityGraphPathConstructor pathConstructor = BpmnPOReachabilityGraphPathConstructor.getInstance(nodeFactory);
        return new StochasticBpmnPORG2StochasticPathLanguageConverterImpl(TransitionSamplingStrategy.getInstance(type), stopper, pathConstructor, maxPathLength);
    }

    static StochasticBpmnPORG2StochasticPathLanguageConverter getInstance() {
        return getInstance(
                TransitionSamplingStrategy.getDefaultType(),
                SamplingStoppingCriterion.getInstance(), 1000);
    }

    BpmnStochasticPOPathLanguage convert(ReachabilityGraph transitionSystem);
}
