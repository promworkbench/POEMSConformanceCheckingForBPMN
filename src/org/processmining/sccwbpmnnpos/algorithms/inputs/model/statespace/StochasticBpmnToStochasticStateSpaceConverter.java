package org.processmining.sccwbpmnnpos.algorithms.inputs.model.statespace;

import org.processmining.models.graphbased.directed.transitionsystem.TransitionSystem;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticBPMNDiagram;

public interface StochasticBpmnToStochasticStateSpaceConverter {
    Object convert(StochasticBPMNDiagram stochasticBpmnDiagram);
}
