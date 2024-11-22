package org.processmining.sccwbpmnnpos.algorithms.inputs.model;

import org.processmining.sccwbpmnnpos.models.utils.activity.Activity;
import org.processmining.sccwbpmnnpos.models.utils.trace.partial_order.PartiallyOrderedTrace;
import org.processmining.sccwbpmnnpos.models.stochastic_language.StochasticLanguage;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticBPMNDiagram;

public interface StochasticBpmnToStochasticLanguageConverter {
    StochasticLanguage<Activity, PartiallyOrderedTrace> convert(final StochasticBPMNDiagram bpmnDiagram);
}
