package org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.node.event;

import org.processmining.models.graphbased.directed.bpmn.BPMNDiagram;
import org.processmining.models.graphbased.directed.bpmn.BPMNNode;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.firable.alternatives.BpmnNodeFiringOption;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.firable.alternatives.MarkingBpmnNodeFiringOption;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.marking.factory.BpmnMarkingFactory;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.marking.utils.BpmnMarkingUtils;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.node.AbstractExecutableBpmnNode;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.marking.BpmnMarking;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.marking.token.factory.BpmnTokenFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ExecutableBpmnStartEventNode extends AbstractExecutableBpmnNode {
    private final Collection<BpmnMarking> consumeOptions;
    private final Collection<BpmnMarking> produceOptions;

    public ExecutableBpmnStartEventNode(BPMNDiagram model, BPMNNode node, BpmnMarkingUtils markingUtils, BpmnTokenFactory tokenFactory, BpmnMarkingFactory markingFactory) {
        super(model, node, markingUtils);
        consumeOptions = Collections.singleton(markingFactory.getEmpty(model));
        produceOptions = Collections.singleton(markingFactory.create(model, model.getOutEdges(getNode()).stream().map(tokenFactory::create).collect(Collectors.toSet())));
    }

    @Override
    public Collection<BpmnMarking> getConsumeOptions() {
        return consumeOptions;
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
        return 0;
    }

    @Override
    public List<List<BpmnNodeFiringOption>> getFiringOptions(BpmnMarking marking) {
        if (marking.isEmpty()) {
            return Collections.singletonList(Collections.singletonList(new MarkingBpmnNodeFiringOption(this, marking,
                    getProduceOptions().iterator().next())));
        }
        return Collections.emptyList();
    }

    public List<BpmnNodeFiringOption> getDefaultFiringOption(BpmnMarking marking) {
        if (marking.isEmpty()) {
            return Collections.singletonList(new MarkingBpmnNodeFiringOption(this, marking,
                    getProduceOptions().iterator().next()));
        }
        return Collections.emptyList();
    }
}
