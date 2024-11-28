package org.processmining.sccwbpmnnpos.models.bpmn.execution.node.factory;

import org.processmining.models.graphbased.directed.bpmn.BPMNDiagram;
import org.processmining.models.graphbased.directed.bpmn.BPMNNode;
import org.processmining.models.graphbased.directed.bpmn.elements.Activity;
import org.processmining.models.graphbased.directed.bpmn.elements.Event;
import org.processmining.models.graphbased.directed.bpmn.elements.Gateway;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.factory.BpmnMarkingFactory;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.token.factory.BpmnTokenFactory;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.utils.BpmnMarkingUtils;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.node.*;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.node.event.ExecutableBpmnEndEventNode;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.node.event.ExecutableBpmnStartEventNode;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.node.gateway.ExecutableBpmnExclusiveGatewayNode;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.node.gateway.ExecutableBpmnParallelGatewayNode;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.node.task.activity.ExecutableBpmnActivityNode;

import java.util.Objects;

public class SimpleExecutableBpmnNodeFactory implements ExecutableBpmnNodeFactory {
    private final BpmnTokenFactory tokenFactory;
    private final BpmnMarkingFactory markingFactory;
    private final BpmnMarkingUtils markingUtils;

    public SimpleExecutableBpmnNodeFactory(BpmnTokenFactory tokenFactory, BpmnMarkingFactory markingFactory, BpmnMarkingUtils markingUtils) {
        this.tokenFactory = tokenFactory;
        this.markingFactory = markingFactory;
        this.markingUtils = markingUtils;
    }

    @Override
    public ExecutableBpmnNode create(BPMNNode node) {
        assert Objects.nonNull(node);
        BPMNDiagram model = (BPMNDiagram) node.getGraph();
        if (node instanceof Gateway) {
            final Gateway gateway = (Gateway) node;
            switch (gateway.getGatewayType()) {
                case DATABASED:
                    return new ExecutableBpmnExclusiveGatewayNode(model, gateway, markingUtils, tokenFactory, markingFactory);
                case PARALLEL:
                    return new ExecutableBpmnParallelGatewayNode(model, gateway, markingUtils, tokenFactory, markingFactory);
                case EVENTBASED:
                case INCLUSIVE:
                case COMPLEX:
                default:
                    throw new IllegalArgumentException(node.getLabel() + " " + gateway.getGatewayType().toString());
            }
        } else if (node instanceof Event) {
            final Event event = (Event) node;
            switch (event.getEventType()) {
                case START:
                    return new ExecutableBpmnStartEventNode(model, event, markingUtils, tokenFactory, markingFactory);
                case END:
                    return new ExecutableBpmnEndEventNode(model, event, markingUtils, tokenFactory, markingFactory);
                case INTERMEDIATE:
                default:
                    throw new IllegalArgumentException(node.getLabel() + " " + event.getEventType().toString());
            }
        } else if (node instanceof Activity) {
            return new ExecutableBpmnActivityNode(model, node, markingUtils, tokenFactory, markingFactory);
        } else {
            throw new IllegalArgumentException(node.getLabel());
        }
    }
}
