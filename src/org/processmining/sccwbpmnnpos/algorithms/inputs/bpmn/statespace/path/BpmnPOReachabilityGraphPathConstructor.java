package org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.statespace.path;

import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.BpmnMarking;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.node.factory.ExecutableBpmnNodeFactory;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.path.BpmnPartiallyOrderedPath;

public interface BpmnPOReachabilityGraphPathConstructor {
    static BpmnPOReachabilityGraphPathConstructor getInstance(ExecutableBpmnNodeFactory nodeFactory) {
        return new BpmnPOReachabilityGraphPathConstructor2(nodeFactory);
    }

    BpmnPartiallyOrderedPath construct(BpmnPartiallyOrderedPath basePath, BpmnPartiallyOrderedPath additionPath,
                                       BpmnMarking marking);
}
