package org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.marking.token.factory;

import org.processmining.models.graphbased.directed.bpmn.BPMNEdge;
import org.processmining.models.graphbased.directed.bpmn.BPMNNode;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.marking.token.BpmnToken;

public interface BpmnTokenFactory {
    static BpmnTokenFactory getInstance() {
        return new SimpleBpmnTokenFactory();
    }

    BpmnToken create(final BPMNEdge<? extends BPMNNode, ? extends BPMNNode> edge);
}
