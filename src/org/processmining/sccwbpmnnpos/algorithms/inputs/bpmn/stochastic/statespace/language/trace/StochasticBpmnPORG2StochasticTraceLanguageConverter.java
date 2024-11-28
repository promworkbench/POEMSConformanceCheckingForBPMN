package org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.stochastic.statespace.language.trace;

import org.processmining.models.graphbased.directed.transitionsystem.ReachabilityGraph;
import org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.execution.BpmnPOPath2TraceConverter;
import org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.stochastic.statespace.language.path.StochasticBpmnPORG2StochasticPathLanguageConverter;
import org.processmining.sccwbpmnnpos.algorithms.utils.stochastics.graph.StochasticGraphPathSamplingStrategy;
import org.processmining.sccwbpmnnpos.models.bpmn.stochastic.language.trace.BpmnStochasticPOTraceLanguage;
import org.processmining.sccwbpmnnpos.models.utils.activity.factory.ActivityFactory;

public interface StochasticBpmnPORG2StochasticTraceLanguageConverter {
    static StochasticBpmnPORG2StochasticTraceLanguageConverter getInstance(ActivityFactory activityFactory, StochasticGraphPathSamplingStrategy.Type type) {
        return new StochasticBpmnPORG2StochasticTraceFromPathLanguage(StochasticBpmnPORG2StochasticPathLanguageConverter.getInstance(type), BpmnPOPath2TraceConverter.getInstance(activityFactory));
    }

    BpmnStochasticPOTraceLanguage convert(ReachabilityGraph transitionSystem);
}
