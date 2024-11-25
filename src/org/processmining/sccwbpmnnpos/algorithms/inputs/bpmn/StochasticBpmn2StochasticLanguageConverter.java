package org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn;

import org.processmining.sccwbpmnnpos.models.utils.activity.Activity;
import org.processmining.sccwbpmnnpos.models.utils.trace.partial_order.PartiallyOrderedTrace;
import org.processmining.sccwbpmnnpos.models.stochastic_language.StochasticLanguage;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticBPMNDiagram;

public interface StochasticBpmn2StochasticLanguageConverter {
    StochasticLanguage<Activity, PartiallyOrderedTrace> convert(final StochasticBPMNDiagram bpmnDiagram);
}
