package org.processmining.sccwbpmnnpos.models.bpmn.execution.path.firing_event;

import java.util.Collection;
import java.util.List;

public class BpmnUnavoidableLiveLockDetected extends Exception {
    private final List<? extends Object> path;

    public BpmnUnavoidableLiveLockDetected(List<? extends Object> path) {
        super(String.format("Unavoidable live lock detected on the following path: %s", path));
        this.path = path;
    }

    public BpmnUnavoidableLiveLockDetected(List<? extends Object> path, Throwable cause) {
        super(String.format("Unavoidable live lock detected on the following path: %s", path), cause);
        this.path = path;
    }
}
