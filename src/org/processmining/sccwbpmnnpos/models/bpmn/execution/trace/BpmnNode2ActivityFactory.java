package org.processmining.sccwbpmnnpos.models.bpmn.execution.trace;

import org.processmining.sccwbpmnnpos.models.utils.activity.Activity;

public interface BpmnNode2ActivityFactory {
    Activity create(org.processmining.models.graphbased.directed.bpmn.elements.Activity bpmnElement);
}
