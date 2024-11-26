package org.processmining.sccwbpmnnpos.models.bpmn.stochastic.language;

import org.processmining.models.graphbased.directed.bpmn.BPMNNode;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.path.BpmnPartiallyOrderedPath;
import org.processmining.sccwbpmnnpos.models.stochastic.language.LinkedListStochasticLanguage;

public class BpmnStochasticPathLanguageImpl extends LinkedListStochasticLanguage<BPMNNode,
        BpmnPartiallyOrderedPath> implements BpmnStochasticPathLanguage {
}
