package org.processmining.poemsconformancecheckingforbpmn.algorithms.conformance_checking.poems.bpmn;

import org.deckfour.xes.model.XLog;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.conformance_checking.ConformanceChecking;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.bpmn.statespace.BpmnNoOptionToCompleteException;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.bpmn.statespace.BpmnUnboundedException;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.conformance.result.POEMSConformanceCheckingResult;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticBPMNDiagram;

public interface BpmnPoemsConformanceChecking extends ConformanceChecking<StochasticBPMNDiagram, XLog> {
    POEMSConformanceCheckingResult calculateConformance(final StochasticBPMNDiagram model, final XLog log) throws BpmnNoOptionToCompleteException, BpmnUnboundedException, InterruptedException;
}
