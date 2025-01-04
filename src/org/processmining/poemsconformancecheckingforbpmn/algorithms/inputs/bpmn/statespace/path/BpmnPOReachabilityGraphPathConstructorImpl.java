package org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.bpmn.statespace.path;

import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import org.processmining.models.graphbased.directed.bpmn.BPMNNode;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.marking.BpmnMarking;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.marking.token.BpmnToken;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.node.ExecutableBpmnNode;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.node.factory.ExecutableBpmnNodeFactory;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.path.BpmnPartiallyOrderedPath;
import org.processmining.poemsconformancecheckingforbpmn.models.utils.ordered_set.exceptions.PartialOrderLoopNotAllowedException;

public class BpmnPOReachabilityGraphPathConstructorImpl implements BpmnPOReachabilityGraphPathConstructor {
    private final ExecutableBpmnNodeFactory executableNodeFactory;

    public BpmnPOReachabilityGraphPathConstructorImpl(ExecutableBpmnNodeFactory executableNodeFactory) {
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
            throw new RuntimeException("This should not happen because we are merging repetitive partial orders", e);
        }

        if (marking.isInitial()) {
            return newPath;
        }

        TObjectIntMap<BPMNNode> tokensProduced = new TObjectIntHashMap<>();
        for (BpmnToken token : marking) {
            tokensProduced.adjustOrPutValue(token.getSourceNode(), 1, 1);
        }

        TObjectIntMap<BPMNNode> tokensConsumed = new TObjectIntHashMap<>();
        for (BpmnToken token : marking) {
            tokensConsumed.adjustOrPutValue(token.getSinkNode(), 1, 1);
        }

        TObjectIntMap<BPMNNode> sourceCount = new TObjectIntHashMap<>();
        TObjectIntMap<BPMNNode> targetCount = new TObjectIntHashMap<>();

        for (BpmnToken token : marking) {

            BPMNNode source = token.getSourceNode();
            ExecutableBpmnNode executableSource = executableNodeFactory.create(source);
            int localSourceIndex = sourceCount.adjustOrPutValue(source, 1, 1);
            int sourceTimesFired =
                    (int) Math.ceil(1.0 * tokensProduced.get(source) / executableSource.getProducesTokensCount());
            int sourceFiringIndex = (int) Math.ceil(1.0 * localSourceIndex / executableSource.getProducesTokensCount());
            int sourceIndex = basePath.getTimesFired(source) - sourceTimesFired + sourceFiringIndex;

            BPMNNode target = token.getSinkNode();
            ExecutableBpmnNode executableTarget = executableNodeFactory.create(target);
            int localTargetIndex = targetCount.adjustOrPutValue(target, 1, 1);
            int firingTargetIndex = (int) Math.ceil(1.0 * localTargetIndex / executableTarget.getConsumesTokensCount());
            int targetIndex = basePath.getTimesFired(target) + firingTargetIndex;

            try {
                newPath.connect(source, sourceIndex, target, targetIndex);
            } catch (PartialOrderLoopNotAllowedException e) {
                throw new RuntimeException("This should not happen because we are merging repetitive partial orders",
                        e);
            }
        }
        return newPath;
    }
}
