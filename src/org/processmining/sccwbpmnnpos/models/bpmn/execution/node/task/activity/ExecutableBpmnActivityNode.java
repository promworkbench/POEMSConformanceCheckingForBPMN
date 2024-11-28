package org.processmining.sccwbpmnnpos.models.bpmn.execution.node.task.activity;

import org.processmining.models.graphbased.directed.bpmn.BPMNDiagram;
import org.processmining.models.graphbased.directed.bpmn.BPMNNode;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.firable.alternatives.BpmnNodeFiringOption;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.firable.alternatives.MarkingBpmnNodeFiringOption;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.BpmnMarking;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.factory.BpmnMarkingFactory;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.token.factory.BpmnTokenFactory;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.utils.BpmnMarkingUtils;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.node.AbstractExecutableBpmnNode;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public class ExecutableBpmnActivityNode extends AbstractExecutableBpmnNode {
    private final Collection<BpmnMarking> consumeOptions;
    private final Collection<BpmnNodeFiringOption> produceOptions;

    public ExecutableBpmnActivityNode(BPMNDiagram model, BPMNNode node, BpmnMarkingUtils markingUtils, BpmnTokenFactory tokenFactory, BpmnMarkingFactory markingFactory) {
        super(model, node, markingUtils);
        consumeOptions = getModel().getInEdges(getNode()).stream().map(e -> markingFactory.create(model,
                Collections.singleton(tokenFactory.create(e)))).collect(Collectors.toList());
        produceOptions = Collections.singleton(new MarkingBpmnNodeFiringOption(this, markingFactory.create(model, model.getOutEdges(getNode()).stream().map(tokenFactory::create).collect(Collectors.toSet()))));
    }

    @Override
    protected Collection<BpmnMarking> getConsumeOptions() {
        return consumeOptions;
    }

    @Override
    public boolean isChoice() {
        return false;
    }

    @Override
    public Collection<BpmnNodeFiringOption> getFiringOptions() {
        return produceOptions;
    }

    @Override
    public int getProducesTokensCount() {
        return produceOptions.iterator().next().getMarking().size();
    }

    @Override
    public int getConsumesTokensCount() {
        return 1;
    }
}
