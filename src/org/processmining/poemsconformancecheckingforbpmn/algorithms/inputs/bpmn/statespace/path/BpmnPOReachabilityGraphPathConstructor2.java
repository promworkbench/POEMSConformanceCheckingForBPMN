package org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.bpmn.statespace.path;

import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.marking.BpmnMarking;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.node.factory.ExecutableBpmnNodeFactory;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.path.BpmnPartiallyOrderedPath;
import org.processmining.poemsconformancecheckingforbpmn.models.utils.ordered_set.exceptions.PartialOrderLoopNotAllowedException;

public class BpmnPOReachabilityGraphPathConstructor2 implements BpmnPOReachabilityGraphPathConstructor {

    public BpmnPOReachabilityGraphPathConstructor2() {
    }

    @Override
    public BpmnPartiallyOrderedPath construct(BpmnPartiallyOrderedPath basePath,
                                              BpmnPartiallyOrderedPath additionPath, BpmnMarking marking) {
        BpmnPartiallyOrderedPath newPath = BpmnPartiallyOrderedPath.getRepetitiveInstance();

        try {
            newPath.concatenate(basePath);
            newPath.concatenate(additionPath);
        } catch (PartialOrderLoopNotAllowedException e) {
            throw new RuntimeException(String.format("This should not happen because we are merging repetitive partial orders. %s", marking), e);
        }
        return newPath;
    }
}