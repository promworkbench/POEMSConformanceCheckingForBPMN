package org.processmining.sccwbpmnnpos.models.bpmn.execution.path;

import org.processmining.models.graphbased.directed.bpmn.BPMNNode;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.path.firing_event.BpmnFiringEvent;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.path.firing_event.BpmnUnavoidableLiveLockDetected;
import org.processmining.sccwbpmnnpos.models.utils.ordered_set.OrderedSet;

public interface BpmnExecutionPath {
    int fire(BPMNNode node);

    int getTimesFired(BPMNNode node);

    BpmnFiringEvent getFiringEvent(BPMNNode node, int executionIndex);

    OrderedSet<BpmnFiringEvent> getOrderedSet();

    void connect(BpmnFiringEvent predecessor, BpmnFiringEvent successor) throws BpmnUnavoidableLiveLockDetected;
}
