package org.processmining.sccwbpmnnpos.models.bpmn.stochastic.language.trace;

import org.processmining.sccwbpmnnpos.models.bpmn.execution.trace.BpmnPartiallyOrderedTrace;
import org.processmining.sccwbpmnnpos.models.stochastic.language.LinkedListStochasticLanguage;
import org.processmining.sccwbpmnnpos.models.utils.activity.Activity;

public class LinkedListBpmnStochasticPOPathLanguage extends LinkedListStochasticLanguage<Activity,
        BpmnPartiallyOrderedTrace> implements BpmnStochasticPOTraceLanguage {
}
