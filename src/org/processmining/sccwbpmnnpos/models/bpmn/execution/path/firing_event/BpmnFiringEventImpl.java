package org.processmining.sccwbpmnnpos.models.bpmn.execution.path.firing_event;

import org.processmining.models.graphbased.directed.bpmn.BPMNNode;

import java.util.Objects;

public class BpmnFiringEventImpl implements BpmnFiringEvent {
    private final BPMNNode node;
    private final int firingIndex;

    public BpmnFiringEventImpl(BPMNNode node, int firingIndex) {
        this.node = node;
        this.firingIndex = firingIndex;
    }

    public BPMNNode getNode() {
        return node;
    }

    public int getFiringIndex() {
        return firingIndex;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof BpmnFiringEventImpl)) return false;
        BpmnFiringEventImpl that = (BpmnFiringEventImpl) object;
        return getFiringIndex() == that.getFiringIndex() && Objects.equals(getNode(), that.getNode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNode(), getFiringIndex());
    }

    @Override
    public String toString() {
        return getNode() + "." + getFiringIndex();
    }
}
