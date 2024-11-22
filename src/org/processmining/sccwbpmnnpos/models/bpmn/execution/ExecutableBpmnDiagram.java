package org.processmining.sccwbpmnnpos.models.bpmn.execution;

import org.processmining.models.graphbased.directed.bpmn.BPMNDiagram;
import org.processmining.models.graphbased.directed.bpmn.BPMNNode;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.node.ExecutableBpmnNode;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.BpmnMarking;

import java.util.Collection;

public interface ExecutableBpmnDiagram {
    ExecutableBpmnNode getExecutableNode(BPMNNode node);

    BPMNDiagram getDiagram();

    Collection<ExecutableBpmnNode> getEnabledNodes(BpmnMarking marking);

    Collection<ExecutableBpmnNode> getStartNodes();
}
