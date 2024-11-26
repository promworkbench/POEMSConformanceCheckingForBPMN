package org.processmining.sccwbpmnnpos.models.bpmn.execution.node;

import org.processmining.models.graphbased.directed.bpmn.BPMNDiagram;
import org.processmining.models.graphbased.directed.bpmn.BPMNNode;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.BpmnFiringChange;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.BpmnFiringChangeImpl;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.firable.alternatives.BpmnNodeFiringOption;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.BpmnMarking;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.utils.BpmnMarkingUtils;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.node.exceptions.BpmnNodeNotEnabledException;

import java.util.Collection;
import java.util.Objects;

public abstract class AbstractExecutableBpmnNode implements ExecutableBpmnNode {
    private final BPMNNode node;
    private final BPMNDiagram model;
    private final BpmnMarkingUtils markingUtils;
    private BpmnNodeFiringOption defaultFiringOption;

    public AbstractExecutableBpmnNode(BPMNDiagram model, BPMNNode node, BpmnMarkingUtils markingUtils) {
        this.node = node;
        this.model = model;
        this.markingUtils = markingUtils;
    }

    @Override
    public BPMNDiagram getModel() {
        return model;
    }

    protected abstract Collection<BpmnMarking> getConsumeOptions();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractExecutableBpmnNode)) return false;
        AbstractExecutableBpmnNode that = (AbstractExecutableBpmnNode) o;
        return Objects.equals(node, that.node) && Objects.equals(model, that.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(node, model);
    }

    public BPMNNode getNode() {
        return node;
    }

    @Override
    public boolean isEnabledIn(BpmnMarking marking) {
        for (BpmnMarking consumeOption : getConsumeOptions()) {
            if (markingUtils.isSubset(marking, consumeOption)) return true;
        }
        return false;
    }

    @Override
    public int getTimesEnabledIn(BpmnMarking marking) {
        int timesEnabled = 0;
        for (BpmnMarking consumeOption : getConsumeOptions()) {
            timesEnabled += markingUtils.isContainedTimes(marking, consumeOption);
        }
        return timesEnabled;
    }

    @Override
    public final BpmnNodeFiringOption getDefaultFiringOption() {
        if (Objects.isNull(defaultFiringOption)) {
            defaultFiringOption = getFiringOptions().iterator().next();
        }
        return defaultFiringOption;
    }

    @Override
    public void setDefaultFiringOption(BpmnNodeFiringOption firingOption) {
        this.defaultFiringOption = firingOption;
    }

    @Override
    public BpmnFiringChange fire(BpmnMarking marking, BpmnNodeFiringOption firingOption, int fireTimes) throws BpmnNodeNotEnabledException {
        final BpmnMarking produceMarking = markingUtils.multiply(firingOption.getMarking(), fireTimes);
        int remainingToConsume = fireTimes;
        BpmnMarking consumeMarking = markingUtils.emptyMarking(getModel());
        for (BpmnMarking consumeOption : getConsumeOptions()) {
            int consumeTimes = markingUtils.isContainedTimes(marking, consumeOption);
            if (remainingToConsume < consumeTimes) {
                consumeTimes = remainingToConsume;
                remainingToConsume = 0;
            } else {
                remainingToConsume -= consumeTimes;
            }
            BpmnMarking totalConsumeOptions = markingUtils.multiply(consumeOption, consumeTimes);
            consumeMarking = markingUtils.sum(consumeMarking, totalConsumeOptions);
            if (remainingToConsume == 0) break;
        }
        final BpmnMarking resultMarking = markingUtils.sum(markingUtils.difference(marking, consumeMarking),
                produceMarking);
        return new BpmnFiringChangeImpl(marking, consumeMarking, produceMarking, resultMarking);
    }

//    protected final boolean hasAtLeastOneIncomingToken(final BpmnMarking marking) {
//        for (BPMNEdge<? extends BPMNNode, ? extends BPMNNode> inEdge : model.getInEdges(node)) {
//            if (marking.contains(tokenFactory.create(inEdge))) return true;
//        }
//        return false;
//    }
//
//    protected final boolean hasAtLeastOneTokenInEveryIncomingEdge(final BpmnMarking marking) {
//        for (BPMNEdge<? extends BPMNNode, ? extends BPMNNode> inEdge : model.getInEdges(node)) {
//            if (!marking.contains(tokenFactory.create(inEdge))) return false;
//        }
//        return true;
//    }
//
//    protected final boolean hasAtLeastOneOutgoingEdge() {
//        return !model.getOutEdges(node).isEmpty();
//    }
//
//    protected final BpmnMarking consumeOneTokenForEachIncomingEdge(final BpmnMarking marking) throws
//    BpmnNodeNotEnabledException {
//        for (BPMNEdge<? extends BPMNNode, ? extends BPMNNode> inEdge : model.getInEdges(node)) {
//            if (!marking.contains(tokenFactory.create(inEdge))) throw new BpmnNodeNotEnabledException(marking, this);
//        }
//        return null;
//    }
//
//    protected final BpmnMarking consumeOneToken(final BpmnMarking marking) throws BpmnNodeNotEnabledException {
//        for (BPMNEdge<? extends BPMNNode, ? extends BPMNNode> inEdge : model.getInEdges(node)) {
//            final BpmnToken token = tokenFactory.create(inEdge);
//            if (marking.contains(token)) return markingFactory.create(model, Collections.singleton(token));
//        }
//        throw new BpmnNodeNotEnabledException(marking, this);
//    }
//
//    protected final BpmnMarking emptyMarking() {
//        return markingFactory.getEmpty(model);
//    }
//
//    protected final BpmnMarking produceOneTokenForEachOutgoingEdge() {
//        return null;
//    }
//
//    protected final BpmnMarking produceOneTokenForAnEdge(final BPMNEdge<? extends BPMNNode, ? extends BPMNNode>
//    outEdge) {
//        assert Objects.equals(node, outEdge.getSource());
//        return null;
//    }


    @Override
    public String toString() {
        return node.toString();
    }
}