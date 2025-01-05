package org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.reachability_graph.stochastic.language.trace;

import org.processmining.models.graphbased.directed.transitionsystem.ReachabilityGraph;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.bpmn.execution.BpmnPOPath2TraceConverter;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.reachability_graph.stochastic.language.path.SPORG2StochasticPathLanguageConverter;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.utils.stochastics.sampling.stopping.SamplingStoppingCriterion;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.utils.stochastics.sampling.stopping.factory.SamplingStoppingCriterionProvider;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.utils.stochastics.sampling.strategy.transition.TansitionSamplingStrategyType;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.stochastic.language.trace.BpmnStochasticPOTraceLanguage;
import org.processmining.poemsconformancecheckingforbpmn.models.utils.activity.factory.ActivityFactory;

public interface SPORG2StochasticTraceLanguageConverter {
    static SPORG2StochasticTraceLanguageConverter getInstance(
            ActivityFactory activityFactory,
            TansitionSamplingStrategyType type,
            SamplingStoppingCriterion stopper
    ) {
        return new SPORG2StochasticTraceFromPathLanguage(
                SPORG2StochasticPathLanguageConverter.getInstance(
                        type,
                        stopper
                ),
                BpmnPOPath2TraceConverter.getInstance(activityFactory)
        );
    }

    static SPORG2StochasticTraceLanguageConverter getInstance(
            ActivityFactory activityFactory,
            TansitionSamplingStrategyType type,
            SamplingStoppingCriterionProvider stoppingCriterionProvider
    ) {
        return new SPORG2StochasticTraceFromPathLanguage(
                SPORG2StochasticPathLanguageConverter.getInstance(
                        type,
                        stoppingCriterionProvider
                ),
                BpmnPOPath2TraceConverter.getInstance(activityFactory)
        );
    }

    BpmnStochasticPOTraceLanguage convert(ReachabilityGraph transitionSystem);
}
