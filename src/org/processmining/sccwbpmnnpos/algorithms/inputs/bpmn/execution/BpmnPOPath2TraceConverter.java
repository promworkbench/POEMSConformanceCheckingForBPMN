package org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.execution;

import org.processmining.sccwbpmnnpos.models.bpmn.execution.path.BpmnPartiallyOrderedPath;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.trace.BpmnModelTrace;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.trace.BpmnNode2ActivityFactory;

public interface BpmnPOPath2TraceConverter {


    BpmnModelTrace convert(BpmnPartiallyOrderedPath path);
}
