package org.processmining.sccwbpmnnpos.models.bpmn.execution.firable.alternatives;

import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.BpmnMarking;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.node.ExecutableBpmnNode;

public interface BpmnNodeFiringOption {
    ExecutableBpmnNode getNode();

    String getLabel();

    BpmnMarking getMarking();

    String toString();
}
