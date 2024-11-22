package org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.factory;

import org.processmining.models.graphbased.directed.bpmn.BPMNDiagram;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.BpmnMarking;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.MultisetBpmnMarking;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.token.BpmnToken;
import org.processmining.sccwbpmnnpos.models.utils.multiset.Multiset;
import org.processmining.sccwbpmnnpos.models.utils.multiset.factory.MultisetFactory;

import java.util.Collection;
import java.util.Collections;

public class DefaultBpmnMarkingFactory implements BpmnMarkingFactory {
    private final MultisetFactory multisetFactory;

    public DefaultBpmnMarkingFactory(MultisetFactory multisetFactory) {
        this.multisetFactory = multisetFactory;
    }

    @Override
    public BpmnMarking create(BPMNDiagram model, Collection<BpmnToken> tokens) {
        return createWithoutCopy(model, multisetFactory.getDefault(tokens));
    }

    @Override
    public BpmnMarking createWithoutCopy(BPMNDiagram model, Multiset<BpmnToken> tokens) {
        return new MultisetBpmnMarking(model, tokens);
    }

    @Override
    public BpmnMarking copy(BpmnMarking marking) {
        return create(marking.getModel(), marking);
    }

    @Override
    public BpmnMarking getEmpty(BPMNDiagram model) {
        return create(model, Collections.emptyList());
    }
}
