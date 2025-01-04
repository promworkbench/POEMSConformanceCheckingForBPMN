package org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.bpmn.statespace;

import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.path.BpmnPartiallyOrderedPath;

import java.util.Objects;

public class BpmnReachabilityGraphEdge {
    public BpmnPartiallyOrderedPath getPath() {
        return path;
    }

    private final BpmnPartiallyOrderedPath path;

    public BpmnReachabilityGraphEdge(BpmnPartiallyOrderedPath path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return path.toString();
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof BpmnReachabilityGraphEdge)) return false;
        BpmnReachabilityGraphEdge that = (BpmnReachabilityGraphEdge) object;
        return Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(path);
    }
}
