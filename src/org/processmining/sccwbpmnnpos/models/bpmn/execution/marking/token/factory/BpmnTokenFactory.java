package org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.token.factory;

import org.processmining.models.graphbased.directed.bpmn.BPMNEdge;
import org.processmining.models.graphbased.directed.bpmn.BPMNNode;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.token.BpmnToken;

public interface BpmnTokenFactory {
    BpmnToken create(final BPMNEdge<? extends BPMNNode, ? extends BPMNNode> edge);
}
