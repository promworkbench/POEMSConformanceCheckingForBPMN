package org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.bpmn.statespace.path;

import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.marking.BpmnMarking;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.node.factory.ExecutableBpmnNodeFactory;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.path.BpmnPartiallyOrderedPath;
import org.processmining.poemsconformancecheckingforbpmn.models.utils.ordered_set.exceptions.PartialOrderLoopNotAllowedException;

public class BpmnPOReachabilityGraphPathConstructor2 implements BpmnPOReachabilityGraphPathConstructor {
    private final ExecutableBpmnNodeFactory executableNodeFactory;

    public BpmnPOReachabilityGraphPathConstructor2(ExecutableBpmnNodeFactory executableNodeFactory) {
        this.executableNodeFactory = executableNodeFactory;
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

//        if (marking.isInitial()) {
//            return newPath;
//        }
//
//        TObjectIntMap<BPMNNode> tokensProduced = new TObjectIntHashMap<>();
//        for (BpmnToken token : marking) {
//            tokensProduced.adjustOrPutValue(token.getSourceNode(), 1, 1);
//        }
//
//        TObjectIntMap<BPMNNode> tokensConsumed = new TObjectIntHashMap<>();
//        for (BpmnToken token : marking) {
//            tokensConsumed.adjustOrPutValue(token.getSinkNode(), 1, 1);
//        }
//
//        TObjectIntMap<BPMNNode> sourceCount = new TObjectIntHashMap<>();
//        TObjectIntMap<BPMNNode> targetCount = new TObjectIntHashMap<>();
//
//
//        for (ReadOnlyMultiset.Entry<BpmnToken> place : marking.entrySet()) {
//            BpmnToken token = place.getElement();
//
//            BPMNNode source = token.getSourceNode();
//            BPMNNode target = token.getSinkNode();
//
//            ExecutableBpmnNode executableSource = executableNodeFactory.create(source);
//            ExecutableBpmnNode executableTarget = executableNodeFactory.create(target);
//
//            for (int i = 1; i <= place.getCount(); i++) {
//                int localSourceIndex = sourceCount.adjustOrPutValue(source, 1, 1);
//                int localTargetIndex = targetCount.adjustOrPutValue(target, 1, 1);
//
//                int sourceIndex;
//                if (executableSource.getProducesTokensCount() > 1) {
//                    sourceIndex = basePath.getTimesFired(source) - place.getCount() + i;
//                } else {
//                    sourceIndex = basePath.getTimesFired(source) - tokensProduced.get(source) + localSourceIndex;
//                }
//
//                int targetIndex;
//                if (executableTarget.getConsumesTokensCount() > 1) {
//                    targetIndex = basePath.getTimesFired(target) + i;
//                } else {
//                    targetIndex = basePath.getTimesFired(target) + localTargetIndex;
//                }
//
//                try {
//                    newPath.connect(source, sourceIndex, target, targetIndex);
//                } catch (PartialOrderLoopNotAllowedException e) {
//                    throw new RuntimeException("This should not happen because we are merging repetitive partial " +
//                            "orders",
//                            e);
//                }
//            }
//        }
        return newPath;
    }
}