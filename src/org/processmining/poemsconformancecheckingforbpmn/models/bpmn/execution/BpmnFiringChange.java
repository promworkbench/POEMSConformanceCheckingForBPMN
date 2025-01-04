package org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution;

import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.marking.BpmnMarking;

public interface BpmnFiringChange {
    BpmnMarking getOriginalMarking();

    BpmnMarking getConsumedMarking();

    BpmnMarking getProducedMarking();

    BpmnMarking getResultMarking();
}
