package org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.token;

import org.processmining.models.graphbased.directed.bpmn.BPMNEdge;
import org.processmining.models.graphbased.directed.bpmn.BPMNNode;

import java.util.Objects;

public class BpmnEdgeToken implements BpmnToken {
    private final BPMNEdge<? extends BPMNNode, ? extends BPMNNode> edge;
    private final transient int hash;

    public BpmnEdgeToken(final BPMNEdge<? extends BPMNNode, ? extends BPMNNode> edge) {
        this.edge = edge;
        this.hash = edge.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BpmnEdgeToken)) return false;
        BpmnEdgeToken that = (BpmnEdgeToken) o;
        return Objects.equals(edge, that.edge);
    }

    @Override
    public int hashCode() {
        return hash;
    }

    @Override
    public BPMNEdge<? extends BPMNNode, ? extends BPMNNode> getEdge() {
        return edge;
    }

    @Override
    public boolean isInEdge(BPMNEdge<? extends BPMNNode, ? extends BPMNNode> edge) {
        return Objects.equals(edge, this.edge);
    }

    @Override
    public BPMNNode getSinkNode() {
        return edge.getTarget();
    }

    @Override
    public BPMNNode getSourceNode() {
        return edge.getSource();
    }

    @Override
    public String toString() {
        return edge.getLabel();
    }
}
