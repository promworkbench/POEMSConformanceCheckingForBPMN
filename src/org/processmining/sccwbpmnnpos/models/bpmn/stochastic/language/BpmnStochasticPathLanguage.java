package org.processmining.sccwbpmnnpos.models.bpmn.stochastic.language;

import org.processmining.models.graphbased.directed.bpmn.BPMNNode;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.path.BpmnPartiallyOrderedPath;
import org.processmining.sccwbpmnnpos.models.stochastic.language.StochasticLanguage;

public interface BpmnStochasticPathLanguage extends StochasticLanguage<BPMNNode, BpmnPartiallyOrderedPath> {
}
