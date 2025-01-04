package org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution;

import org.processmining.models.graphbased.directed.bpmn.BPMNDiagram;
import org.processmining.models.graphbased.directed.bpmn.BPMNNode;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.node.ExecutableBpmnNode;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.marking.BpmnMarking;

import java.util.Collection;

public interface ExecutableBpmnDiagram {
    ExecutableBpmnNode getExecutableNode(BPMNNode node);

    BPMNDiagram getDiagram();

    Collection<ExecutableBpmnNode> getEnabledNodes(BpmnMarking marking);

    Collection<ExecutableBpmnNode> getStartNodes();
}
