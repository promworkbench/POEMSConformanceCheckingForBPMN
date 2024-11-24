package org.processmining.sccwbpmnnpos.models.bpmn.execution.path.firing_event;

import org.processmining.models.graphbased.directed.bpmn.BPMNNode;

public interface BpmnFiringEvent {
    BPMNNode getNode();

    int getFiringIndex();

    boolean equals(Object other);

    int hashCode();

    String toString();
}
