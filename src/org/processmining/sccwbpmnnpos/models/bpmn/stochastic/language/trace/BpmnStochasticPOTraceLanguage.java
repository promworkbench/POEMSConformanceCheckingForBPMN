package org.processmining.sccwbpmnnpos.models.bpmn.stochastic.language.trace;

import org.processmining.sccwbpmnnpos.models.bpmn.execution.trace.BpmnPartiallyOrderedTrace;
import org.processmining.sccwbpmnnpos.models.stochastic.language.StochasticLanguage;
import org.processmining.sccwbpmnnpos.models.stochastic.language.trace.partial.StochasticPOTraceLanguage;
import org.processmining.sccwbpmnnpos.models.utils.activity.Activity;

public interface BpmnStochasticPOTraceLanguage extends StochasticPOTraceLanguage<Activity, BpmnPartiallyOrderedTrace> {
}
