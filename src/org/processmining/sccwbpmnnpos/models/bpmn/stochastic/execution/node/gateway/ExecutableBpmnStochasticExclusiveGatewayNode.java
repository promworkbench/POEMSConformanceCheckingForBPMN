package org.processmining.sccwbpmnnpos.models.bpmn.stochastic.execution.node.gateway;

import org.processmining.sccwbpmnnpos.models.bpmn.execution.firable.alternatives.BpmnNodeFiringOption;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.BpmnMarking;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.factory.BpmnMarkingFactory;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.token.BpmnToken;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.token.factory.BpmnTokenFactory;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.utils.BpmnMarkingUtils;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.node.AbstractExecutableBpmnNode;
import org.processmining.sccwbpmnnpos.models.bpmn.stochastic.execution.firingoptions.StochasticMarkingBpmnNodeFiringOption;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticBPMNDiagram;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticGateway;
import org.processmining.stochasticbpmn.models.stochastic.Probability;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public class ExecutableBpmnStochasticExclusiveGatewayNode extends AbstractExecutableBpmnNode {
    private final Collection<BpmnMarking> consumeOptions;
    private final Collection<BpmnMarking> produceOptions;
    private final StochasticGateway stochasticGateway;

    public ExecutableBpmnStochasticExclusiveGatewayNode(StochasticBPMNDiagram model, StochasticGateway gateway,
                                                        BpmnMarkingUtils markingUtils, BpmnTokenFactory tokenFactory,
                                                        BpmnMarkingFactory markingFactory) {
        super(model, gateway, markingUtils);
        consumeOptions = getModel().getInEdges(getNode()).stream().map(e -> markingFactory.create(model,
                Collections.singleton(tokenFactory.create(e)))).collect(Collectors.toList());
        produceOptions = getModel().getOutEdges(getNode()).stream().map(e -> markingFactory.create(model,
                Collections.singleton(tokenFactory.create(e)))).collect(Collectors.toList());
        this.stochasticGateway = gateway;
    }

    @Override
    public Collection<BpmnMarking> getConsumeOptions() {
        return consumeOptions;
    }

    @Override
    public boolean isChoice() {
        return true;
    }

    @Override
    public Collection<BpmnMarking> getProduceOptions() {
        return new ArrayList<>(produceOptions);
    }

    @Override
    public int getProducesTokensCount() {
        return 1;
    }

    @Override
    public int getConsumesTokensCount() {
        return 1;
    }

    @Override
    protected BpmnNodeFiringOption newFiringOption(BpmnMarking consumeMarking, BpmnMarking produceMarking) {
        Probability probability =
                stochasticGateway.getProbability(produceMarking.stream().map(BpmnToken::getEdge).collect(Collectors.toList()));

        return new StochasticMarkingBpmnNodeFiringOption(this, consumeMarking, produceMarking, probability);
    }
}
