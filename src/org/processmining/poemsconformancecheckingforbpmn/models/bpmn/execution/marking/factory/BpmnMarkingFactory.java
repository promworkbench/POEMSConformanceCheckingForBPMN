package org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.marking.factory;

import org.processmining.models.graphbased.directed.bpmn.BPMNDiagram;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.marking.BpmnMarking;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.marking.token.BpmnToken;
import org.processmining.poemsconformancecheckingforbpmn.models.utils.multiset.Multiset;
import org.processmining.poemsconformancecheckingforbpmn.models.utils.multiset.factory.MultisetFactory;

import java.util.Collection;

public interface BpmnMarkingFactory {
    static BpmnMarkingFactory getInstance() {
        return new DefaultBpmnMarkingFactory(MultisetFactory.getInstance());
    }

    BpmnMarking create(final BPMNDiagram model, Collection<BpmnToken> tokens);

    BpmnMarking createWithoutCopy(final BPMNDiagram model, Multiset<BpmnToken> tokens);

    BpmnMarking copy(BpmnMarking marking);

    BpmnMarking getEmpty(final BPMNDiagram model);
}
