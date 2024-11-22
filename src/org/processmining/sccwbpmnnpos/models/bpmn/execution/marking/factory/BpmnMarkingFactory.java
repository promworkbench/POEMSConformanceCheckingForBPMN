package org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.factory;

import org.processmining.models.graphbased.directed.bpmn.BPMNDiagram;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.BpmnMarking;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.token.BpmnToken;
import org.processmining.sccwbpmnnpos.models.utils.multiset.Multiset;

import java.util.Collection;

public interface BpmnMarkingFactory {
    BpmnMarking create(final BPMNDiagram model, Collection<BpmnToken> tokens);

    BpmnMarking createWithoutCopy(final BPMNDiagram model, Multiset<BpmnToken> tokens);

    BpmnMarking copy(BpmnMarking marking);

    BpmnMarking getEmpty(final BPMNDiagram model);
}
