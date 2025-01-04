package org.processmining.poemsconformancecheckingforbpmn.models.bpmn.stochastic.language.trace;

import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.trace.BpmnPartiallyOrderedTrace;
import org.processmining.poemsconformancecheckingforbpmn.models.stochastic.language.LinkedListStochasticLanguage;
import org.processmining.poemsconformancecheckingforbpmn.models.utils.activity.Activity;

public class LinkedListBpmnStochasticPOPathLanguage extends LinkedListStochasticLanguage<Activity,
        BpmnPartiallyOrderedTrace> implements BpmnStochasticPOTraceLanguage {
}
