package org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.bpmn.stochastic.statespace.language.trace;

import org.processmining.models.graphbased.directed.transitionsystem.ReachabilityGraph;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.bpmn.execution.BpmnPOPath2TraceConverter;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.bpmn.stochastic.statespace.language.path.StochasticBpmnPORG2StochasticPathLanguageConverter;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.utils.stochastics.sampling.stopping.SamplingStoppingCriterion;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.utils.stochastics.sampling.strategy.graph.TansitionSamplingStrategyType;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.stochastic.language.trace.BpmnStochasticPOTraceLanguage;
import org.processmining.poemsconformancecheckingforbpmn.models.utils.activity.factory.ActivityFactory;

public interface StochasticBpmnPORG2StochasticTraceLanguageConverter {
    static StochasticBpmnPORG2StochasticTraceLanguageConverter getInstance(ActivityFactory activityFactory, TansitionSamplingStrategyType type, SamplingStoppingCriterion stopper, int maxPathLength) {
        return new StochasticBpmnPORG2StochasticTraceFromPathLanguage(StochasticBpmnPORG2StochasticPathLanguageConverter.getInstance(type, stopper, maxPathLength), BpmnPOPath2TraceConverter.getInstance(activityFactory));
    }

    BpmnStochasticPOTraceLanguage convert(ReachabilityGraph transitionSystem);
}
