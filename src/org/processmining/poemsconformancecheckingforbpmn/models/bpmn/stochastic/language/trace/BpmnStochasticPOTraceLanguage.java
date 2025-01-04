package org.processmining.poemsconformancecheckingforbpmn.models.bpmn.stochastic.language.trace;

import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.trace.BpmnPartiallyOrderedTrace;
import org.processmining.poemsconformancecheckingforbpmn.models.stochastic.language.trace.partial.StochasticPOTraceLanguage;
import org.processmining.poemsconformancecheckingforbpmn.models.utils.activity.Activity;

public interface BpmnStochasticPOTraceLanguage extends StochasticPOTraceLanguage<Activity, BpmnPartiallyOrderedTrace> {
}
