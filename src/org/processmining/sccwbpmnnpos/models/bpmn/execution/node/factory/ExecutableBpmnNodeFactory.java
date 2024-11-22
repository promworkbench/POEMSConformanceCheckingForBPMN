package org.processmining.sccwbpmnnpos.models.bpmn.execution.node.factory;

import org.processmining.models.graphbased.directed.bpmn.BPMNNode;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.node.ExecutableBpmnNode;

public interface ExecutableBpmnNodeFactory {
    ExecutableBpmnNode create(final BPMNNode node);
}
