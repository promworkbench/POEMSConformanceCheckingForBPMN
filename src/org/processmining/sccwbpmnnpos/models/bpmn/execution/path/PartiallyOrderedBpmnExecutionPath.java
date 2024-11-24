package org.processmining.sccwbpmnnpos.models.bpmn.execution.path;

import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import org.processmining.models.graphbased.directed.bpmn.BPMNNode;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.path.firing_event.BpmnFiringEvent;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.path.firing_event.BpmnFiringEventImpl;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.path.firing_event.BpmnUnavoidableLiveLockDetected;
import org.processmining.sccwbpmnnpos.models.utils.ordered_set.OrderedSet;
import org.processmining.sccwbpmnnpos.models.utils.ordered_set.exceptions.PartialOrderLoopNotAllowedException;
import org.processmining.sccwbpmnnpos.models.utils.ordered_set.partial.ArrayPartiallyOrderedSet;
import org.processmining.sccwbpmnnpos.models.utils.ordered_set.partial.PartiallyOrderedSet;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class PartiallyOrderedBpmnExecutionPath implements BpmnExecutionPath {
    private final PartiallyOrderedSet<BpmnFiringEvent> po;
    private final TObjectIntMap<BPMNNode> lastFiring;

    public PartiallyOrderedBpmnExecutionPath() {
        po = new ArrayPartiallyOrderedSet<>();
        lastFiring = new TObjectIntHashMap<>();
    }

    @Override
    public int fire(BPMNNode node) {
        int firingIndex = lastFiring.adjustOrPutValue(node, 1, 1);
        BpmnFiringEventImpl bpmnFiringEvent = new BpmnFiringEventImpl(node, firingIndex);
        po.add(bpmnFiringEvent);
        return firingIndex;
    }

    @Override
    public int getTimesFired(BPMNNode node) {
        int lastFiringIndex = lastFiring.get(node);
        if (lastFiring.getNoEntryValue() == lastFiringIndex) {
            return 0;
        }
        return lastFiringIndex;
    }

    @Override
    public BpmnFiringEvent getFiringEvent(BPMNNode node, int firingIndex) {
        return new BpmnFiringEventImpl(node, firingIndex);
    }

    @Override
    public OrderedSet<BpmnFiringEvent> getOrderedSet() {
        return po;
    }

    public boolean canConnect(BpmnFiringEvent predecessor, BpmnFiringEvent successor) {
        if (Objects.equals(predecessor, successor) || Objects.equals(predecessor.getNode(), successor.getNode())) {
            return false;
        }
        Set<BpmnFiringEvent> predecessors = po.getPredecessors(predecessor);
        for (BpmnFiringEvent poPredecessor : predecessors) {
            if (poPredecessor.getNode().equals(successor.getNode())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void connect(BpmnFiringEvent predecessor, BpmnFiringEvent successor) throws BpmnUnavoidableLiveLockDetected {
        if (!canConnect(predecessor, successor)) {
            List<BpmnFiringEvent> loopSequence = po.getLoopSequence(new BpmnFiringEventImpl(successor.getNode(), successor.getFiringIndex() - 1), predecessor);
            loopSequence.remove(loopSequence.size() - 1);
            loopSequence.add(successor);
            throw new BpmnUnavoidableLiveLockDetected(loopSequence);
        }
        try {
            po.setPredecessor(successor, predecessor);
        } catch (PartialOrderLoopNotAllowedException e) {
            throw new BpmnUnavoidableLiveLockDetected(e.getLoopSequence(), e);
        }
    }

    @Override
    public String toString() {
        return po.toString();
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof PartiallyOrderedBpmnExecutionPath)) return false;
        PartiallyOrderedBpmnExecutionPath that = (PartiallyOrderedBpmnExecutionPath) object;
        return Objects.equals(po, that.po);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(po);
    }
}
