package org.processmining.sccwbpmnnpos.models.bpmn.stochastic.execution.node.factory;

import org.processmining.models.graphbased.directed.bpmn.BPMNNode;
import org.processmining.models.graphbased.directed.bpmn.elements.Gateway;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.factory.BpmnMarkingFactory;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.token.factory.BpmnTokenFactory;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.utils.BpmnMarkingUtils;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.node.ExecutableBpmnNode;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.node.factory.ExecutableBpmnNodeFactory;
import org.processmining.sccwbpmnnpos.models.bpmn.stochastic.execution.node.gateway.ExecutableBpmnStochasticExclusiveGatewayNode;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticBPMNDiagram;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticGateway;

import java.util.Objects;

public class ExecutableStochasticBpmnNodeFactoryImpl implements ExecutableStochasticBpmnNodeFactory {
    private final ExecutableBpmnNodeFactory factory;
    private final BpmnTokenFactory tokenFactory;
    private final BpmnMarkingFactory markingFactory;
    private final BpmnMarkingUtils markingUtils;

    public ExecutableStochasticBpmnNodeFactoryImpl(ExecutableBpmnNodeFactory factory, BpmnTokenFactory tokenFactory, BpmnMarkingFactory markingFactory, BpmnMarkingUtils markingUtils) {
        this.factory = factory;
        this.tokenFactory = tokenFactory;
        this.markingFactory = markingFactory;
        this.markingUtils = markingUtils;
    }

    @Override
    public ExecutableBpmnNode create(BPMNNode node) {
        assert Objects.nonNull(node);
        if (node instanceof StochasticGateway) {
            StochasticGateway stochasticGateway = (StochasticGateway) node;
            StochasticBPMNDiagram model = (StochasticBPMNDiagram) node.getGraph();
            if (Gateway.GatewayType.DATABASED == stochasticGateway.getGatewayType()) {
                return new ExecutableBpmnStochasticExclusiveGatewayNode(model, stochasticGateway, markingUtils, tokenFactory, markingFactory);
            }
        }
        return factory.create(node);
    }
}
