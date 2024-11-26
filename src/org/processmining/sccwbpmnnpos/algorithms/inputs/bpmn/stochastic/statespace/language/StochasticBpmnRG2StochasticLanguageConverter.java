package org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.stochastic.statespace.language;

import org.processmining.models.graphbased.directed.transitionsystem.ReachabilityGraph;
import org.processmining.sccwbpmnnpos.models.bpmn.stochastic.language.BpmnStochasticPathLanguage;

public interface StochasticBpmnRG2StochasticLanguageConverter {

    BpmnStochasticPathLanguage convert(ReachabilityGraph transitionSystem);
}
