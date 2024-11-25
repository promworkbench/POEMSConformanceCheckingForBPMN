package org.processmining.sccwbpmnnpos.algorithms.conformance_checking;

import org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.statespace.BpmnNoOptionToCompleteException;
import org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.statespace.BpmnUnboundedException;
import org.processmining.sccwbpmnnpos.models.bpmn.conformance.result.ConformanceCheckingResult;

public interface ConformanceChecking<MODEL, LOG> {
    ConformanceCheckingResult calculateConformance(final MODEL model, final LOG log) throws BpmnNoOptionToCompleteException, BpmnUnboundedException;
}
