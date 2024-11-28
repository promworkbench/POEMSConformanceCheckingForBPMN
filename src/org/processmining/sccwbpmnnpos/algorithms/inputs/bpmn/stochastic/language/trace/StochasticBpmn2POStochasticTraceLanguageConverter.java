package org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.stochastic.language.trace;

import org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.statespace.BpmnNoOptionToCompleteException;
import org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.statespace.BpmnUnboundedException;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.trace.BpmnPartiallyOrderedTrace;
import org.processmining.sccwbpmnnpos.models.bpmn.stochastic.language.trace.BpmnStochasticPOTraceLanguage;
import org.processmining.sccwbpmnnpos.models.utils.activity.Activity;
import org.processmining.sccwbpmnnpos.models.stochastic.language.StochasticLanguage;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticBPMNDiagram;

public interface StochasticBpmn2POStochasticTraceLanguageConverter {
    BpmnStochasticPOTraceLanguage convert(final StochasticBPMNDiagram bpmnDiagram) throws BpmnNoOptionToCompleteException, BpmnUnboundedException;
}
