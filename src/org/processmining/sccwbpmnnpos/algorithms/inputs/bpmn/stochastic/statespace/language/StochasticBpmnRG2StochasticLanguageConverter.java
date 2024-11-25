package org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.stochastic.statespace.language;

import org.processmining.models.graphbased.directed.transitionsystem.TransitionSystem;
import org.processmining.sccwbpmnnpos.models.stochastic_language.StochasticLanguage;
import org.processmining.sccwbpmnnpos.models.utils.activity.Activity;
import org.processmining.sccwbpmnnpos.models.utils.trace.partial_order.PartiallyOrderedTrace;

public interface StochasticBpmnRG2StochasticLanguageConverter {
    StochasticLanguage<Activity, PartiallyOrderedTrace> convert(TransitionSystem transitionSystem);
}
