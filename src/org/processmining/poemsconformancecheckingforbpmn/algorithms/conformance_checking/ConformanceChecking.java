package org.processmining.poemsconformancecheckingforbpmn.algorithms.conformance_checking;

import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.bpmn.statespace.BpmnNoOptionToCompleteException;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.bpmn.statespace.BpmnUnboundedException;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.conformance.result.POEMSConformanceCheckingResult;

public interface ConformanceChecking<MODEL, LOG> {
    POEMSConformanceCheckingResult calculateConformance(final MODEL model, final LOG log) throws BpmnNoOptionToCompleteException, BpmnUnboundedException, InterruptedException;
}
