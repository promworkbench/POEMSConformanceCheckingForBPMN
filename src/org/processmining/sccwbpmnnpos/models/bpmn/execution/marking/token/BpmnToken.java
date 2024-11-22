package org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.token;

import org.processmining.models.graphbased.directed.bpmn.BPMNEdge;
import org.processmining.models.graphbased.directed.bpmn.BPMNNode;

public interface BpmnToken {
    BPMNEdge<? extends BPMNNode, ? extends BPMNNode> getEdge();

    boolean isInEdge(BPMNEdge<? extends BPMNNode, ? extends BPMNNode> edge);

    BPMNNode getSinkNode();

    BPMNNode getSourceNode();
}
