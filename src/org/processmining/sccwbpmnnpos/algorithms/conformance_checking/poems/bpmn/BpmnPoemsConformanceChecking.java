package org.processmining.sccwbpmnnpos.algorithms.conformance_checking.poems.bpmn;

import org.deckfour.xes.model.XLog;
import org.processmining.sccwbpmnnpos.algorithms.conformance_checking.ConformanceChecking;
import org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.statespace.BpmnNoOptionToCompleteException;
import org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.statespace.BpmnUnboundedException;
import org.processmining.sccwbpmnnpos.models.bpmn.conformance.result.POEMSConformanceCheckingResult;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticBPMNDiagram;

public interface BpmnPoemsConformanceChecking extends ConformanceChecking<StochasticBPMNDiagram, XLog> {
    POEMSConformanceCheckingResult calculateConformance(final StochasticBPMNDiagram model, final XLog log) throws BpmnNoOptionToCompleteException, BpmnUnboundedException, InterruptedException;
}
