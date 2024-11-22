package org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.token.factory;

import org.processmining.models.graphbased.directed.bpmn.BPMNEdge;
import org.processmining.models.graphbased.directed.bpmn.BPMNNode;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.token.BpmnEdgeToken;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.token.BpmnToken;

public class SimpleBpmnTokenFactory implements BpmnTokenFactory {
    public SimpleBpmnTokenFactory() {
    }

    @Override
    public BpmnToken create(BPMNEdge<? extends BPMNNode, ? extends BPMNNode> edge) {
        return new BpmnEdgeToken(edge);
    }
}
