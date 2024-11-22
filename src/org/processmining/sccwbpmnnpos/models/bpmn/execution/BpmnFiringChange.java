package org.processmining.sccwbpmnnpos.models.bpmn.execution;

import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.BpmnMarking;

public interface BpmnFiringChange {
    BpmnMarking getOriginalMarking();

    BpmnMarking getConsumedMarking();

    BpmnMarking getProducedMarking();

    BpmnMarking getResultMarking();
}
