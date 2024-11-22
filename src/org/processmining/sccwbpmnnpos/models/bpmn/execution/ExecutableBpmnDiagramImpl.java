package org.processmining.sccwbpmnnpos.models.bpmn.execution;

import org.processmining.models.graphbased.directed.bpmn.BPMNDiagram;
import org.processmining.models.graphbased.directed.bpmn.BPMNNode;
import org.processmining.models.graphbased.directed.bpmn.elements.Event;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.BpmnMarking;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.node.ExecutableBpmnNode;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.node.factory.ExecutableBpmnNodeFactory;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class ExecutableBpmnDiagramImpl implements ExecutableBpmnDiagram {
    private final BPMNDiagram diagram;
    private final ExecutableBpmnNodeFactory nodeFactory;

    public ExecutableBpmnDiagramImpl(BPMNDiagram diagram, final ExecutableBpmnNodeFactory nodeFactory) {
        this.diagram = diagram;
        this.nodeFactory = nodeFactory;
    }

    @Override
    public ExecutableBpmnNode getExecutableNode(BPMNNode node) {
        return nodeFactory.create(node);
    }

    @Override
    public BPMNDiagram getDiagram() {
        return diagram;
    }

    @Override
    public Collection<ExecutableBpmnNode> getEnabledNodes(BpmnMarking marking) {
        return marking.getReachableNodes().stream().map(nodeFactory::create).filter(node -> node.isEnabledIn(marking)).collect(Collectors.toSet());
    }

    @Override
    public Collection<ExecutableBpmnNode> getStartNodes() {
        List<ExecutableBpmnNode> startNodes = new LinkedList<>();
        for (Event event : diagram.getEvents()) {
            if (Event.EventType.START.equals(event.getEventType())) {
                ExecutableBpmnNode executableNode = nodeFactory.create(event);
                startNodes.add(executableNode);
            }
        }
        return startNodes;
    }
}
