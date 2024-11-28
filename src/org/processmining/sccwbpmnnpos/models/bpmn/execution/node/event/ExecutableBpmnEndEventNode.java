package org.processmining.sccwbpmnnpos.models.bpmn.execution.node.event;

import org.processmining.models.graphbased.directed.bpmn.BPMNDiagram;
import org.processmining.models.graphbased.directed.bpmn.BPMNNode;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.BpmnFiringChange;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.BpmnFiringChangeImpl;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.firable.alternatives.BpmnNodeFiringOption;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.firable.alternatives.MarkingBpmnNodeFiringOption;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.factory.BpmnMarkingFactory;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.utils.BpmnMarkingUtils;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.node.AbstractExecutableBpmnNode;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.node.exceptions.BpmnNodeNotEnabledException;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.BpmnMarking;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.token.factory.BpmnTokenFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public class ExecutableBpmnEndEventNode extends AbstractExecutableBpmnNode {
    private final Collection<BpmnMarking> consumeOptions;
    private final Collection<BpmnNodeFiringOption> produceOptions;

    public ExecutableBpmnEndEventNode(BPMNDiagram model, BPMNNode node, BpmnMarkingUtils markingUtils, BpmnTokenFactory tokenFactory, BpmnMarkingFactory markingFactory) {
        super(model, node, markingUtils);
        consumeOptions = model.getInEdges(node).stream().map(e -> markingFactory.create(model, Collections.singleton(tokenFactory.create(e)))).collect(Collectors.toSet());
        produceOptions = Collections.singleton(new MarkingBpmnNodeFiringOption(this, markingFactory.getEmpty(model)));
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
        return 0;
    }

    @Override
    public int getConsumesTokensCount() {
        return 1;
    }
}
