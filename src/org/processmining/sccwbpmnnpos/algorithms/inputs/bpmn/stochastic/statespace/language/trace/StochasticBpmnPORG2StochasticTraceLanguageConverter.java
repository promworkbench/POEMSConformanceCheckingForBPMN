package org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.stochastic.statespace.language.trace;

import org.processmining.models.graphbased.directed.transitionsystem.ReachabilityGraph;
import org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.execution.BpmnPOPath2TraceConverter;
import org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.stochastic.statespace.language.path.StochasticBpmnPORG2StochasticPathLanguageConverter;
import org.processmining.sccwbpmnnpos.algorithms.inputs.stochastic_language.stopping.StochasticLanguageGeneratorStopper;
import org.processmining.sccwbpmnnpos.algorithms.utils.stochastics.sampling.strategy.graph.StochasticGraphPathSamplingStrategy;
import org.processmining.sccwbpmnnpos.models.bpmn.stochastic.language.trace.BpmnStochasticPOTraceLanguage;
import org.processmining.sccwbpmnnpos.models.utils.activity.factory.ActivityFactory;

public interface StochasticBpmnPORG2StochasticTraceLanguageConverter {
    static StochasticBpmnPORG2StochasticTraceLanguageConverter getInstance(ActivityFactory activityFactory, StochasticGraphPathSamplingStrategy.GraphSamplingType type, StochasticLanguageGeneratorStopper stopper, int maxPathLength) {
        return new StochasticBpmnPORG2StochasticTraceFromPathLanguage(StochasticBpmnPORG2StochasticPathLanguageConverter.getInstance(type, stopper, maxPathLength), BpmnPOPath2TraceConverter.getInstance(activityFactory));
    }

    BpmnStochasticPOTraceLanguage convert(ReachabilityGraph transitionSystem);
}
