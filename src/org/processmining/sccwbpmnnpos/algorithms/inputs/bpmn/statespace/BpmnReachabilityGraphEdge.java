package org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.statespace;

import org.processmining.sccwbpmnnpos.models.bpmn.execution.path.BpmnExecutionPath;

import java.util.Objects;

public class BpmnReachabilityGraphEdge {
    public BpmnExecutionPath getPath() {
        return path;
    }

    private final BpmnExecutionPath path;

    public BpmnReachabilityGraphEdge(BpmnExecutionPath path) {
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
