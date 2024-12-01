package org.processmining.sccwbpmnnpos.models.bpmn.execution.node.gateway;

import org.processmining.models.graphbased.directed.bpmn.BPMNDiagram;
import org.processmining.models.graphbased.directed.bpmn.BPMNNode;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.firable.alternatives.BpmnNodeFiringOption;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.firable.alternatives.MarkingBpmnNodeFiringOption;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.factory.BpmnMarkingFactory;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.utils.BpmnMarkingUtils;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.BpmnMarking;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.token.factory.BpmnTokenFactory;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.node.AbstractExecutableBpmnNode;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public class ExecutableBpmnParallelGatewayNode extends AbstractExecutableBpmnNode {
    private final Collection<BpmnMarking> consumeOptions;
    private final Collection<BpmnMarking> produceOptions;

    public ExecutableBpmnParallelGatewayNode(BPMNDiagram model, BPMNNode node, BpmnMarkingUtils markingUtils, BpmnTokenFactory tokenFactory, BpmnMarkingFactory markingFactory) {
        super(model, node, markingUtils);
        consumeOptions = Collections.singleton(markingFactory.create(model, model.getInEdges(node).stream().map(tokenFactory::create).collect(Collectors.toSet())));
        produceOptions = Collections.singleton(markingFactory.create(model, model.getOutEdges(node).stream().map(tokenFactory::create).collect(Collectors.toSet())));
    }

    @Override
    public boolean isChoice() {
        return false;
    }

    @Override
    public Collection<BpmnMarking> getProduceOptions() {
        return produceOptions;
    }

    @Override
    public int getProducesTokensCount() {
        return produceOptions.iterator().next().size();
    }

    @Override
    public int getConsumesTokensCount() {
        return consumeOptions.iterator().next().size();
    }

    @Override
    public Collection<BpmnMarking> getConsumeOptions() {
        return consumeOptions;
    }


}
