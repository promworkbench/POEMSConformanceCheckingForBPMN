package org.processmining.sccwbpmnnpos.models.bpmn.execution.trace;

import org.processmining.sccwbpmnnpos.models.utils.activity.Activity;
import org.processmining.sccwbpmnnpos.models.utils.ordered_set.partial.eventbased.RepetitiveEventBasedPartiallyOrderedSet;
import org.processmining.sccwbpmnnpos.models.utils.trace.partial_order.PartiallyOrderedTrace;

public class BpmnModelTraceImpl extends RepetitiveEventBasedPartiallyOrderedSet<Activity> implements BpmnModelTrace {
}
