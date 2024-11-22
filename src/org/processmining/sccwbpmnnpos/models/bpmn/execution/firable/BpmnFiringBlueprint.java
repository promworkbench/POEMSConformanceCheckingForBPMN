package org.processmining.sccwbpmnnpos.models.bpmn.execution.firable;

import org.processmining.sccwbpmnnpos.models.bpmn.execution.node.ExecutableBpmnNode;

public interface BpmnFiringBlueprint {
    ExecutableBpmnNode getNode();

    String getData();
}
