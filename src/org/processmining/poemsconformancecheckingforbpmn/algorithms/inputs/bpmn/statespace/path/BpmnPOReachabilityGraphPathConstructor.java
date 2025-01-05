package org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.bpmn.statespace.path;

import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.marking.BpmnMarking;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.node.factory.ExecutableBpmnNodeFactory;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.path.BpmnPartiallyOrderedPath;

public interface BpmnPOReachabilityGraphPathConstructor {
    static BpmnPOReachabilityGraphPathConstructor getInstance(ExecutableBpmnNodeFactory nodeFactory) {
        return new BpmnPOReachabilityGraphPathConstructor2();
    }

    BpmnPartiallyOrderedPath construct(BpmnPartiallyOrderedPath basePath, BpmnPartiallyOrderedPath additionPath,
                                       BpmnMarking marking);
}
