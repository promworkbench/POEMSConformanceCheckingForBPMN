package org.processmining.sccwbpmnnpos.models.bpmn.stochastic.execution.node.gateway;

import org.processmining.models.graphbased.directed.bpmn.BPMNDiagram;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.firable.alternatives.BpmnNodeFiringOption;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.BpmnMarking;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.factory.BpmnMarkingFactory;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.token.factory.BpmnTokenFactory;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.utils.BpmnMarkingUtils;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.node.AbstractExecutableBpmnNode;
import org.processmining.sccwbpmnnpos.models.bpmn.stochastic.execution.firingoptions.StochasticBpmnNodeFiringOption;
import org.processmining.sccwbpmnnpos.models.bpmn.stochastic.execution.firingoptions.StochasticMarkingBpmnNodeFiringOption;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticBPMNDiagram;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticGateway;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public class ExecutableBpmnStochasticExclusiveGatewayNode extends AbstractExecutableBpmnNode {
    private final Collection<BpmnMarking> consumeOptions;
    private final Collection<StochasticBpmnNodeFiringOption> produceOptions;

    public ExecutableBpmnStochasticExclusiveGatewayNode(StochasticBPMNDiagram model, StochasticGateway gateway,
                                                        BpmnMarkingUtils markingUtils,
                                                        BpmnTokenFactory tokenFactory,
                                                        BpmnMarkingFactory markingFactory) {
        super(model, gateway, markingUtils);
        consumeOptions = getModel().getInEdges(getNode()).stream().map(e ->
                markingFactory.create(model, Collections.singleton(tokenFactory.create(e)))).collect(Collectors.toList());
        produceOptions =
                getModel().getOutEdges(getNode()).stream().map(e -> new StochasticMarkingBpmnNodeFiringOption(this,
                markingFactory.create(model, Collections.singleton(tokenFactory.create(e))),
                        gateway.getProbability(e))).collect(Collectors.toList());
    }

    @Override
    protected Collection<BpmnMarking> getConsumeOptions() {
        return consumeOptions;
    }

    @Override
    public boolean isChoice() {
        return true;
    }

    @Override
    public Collection<BpmnNodeFiringOption> getFiringOptions() {
        return new ArrayList<>(produceOptions);
    }
}
