package org.processmining.sccwbpmnnpos.models.bpmn.execution;

import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.BpmnMarking;

public class BpmnFiringChangeImpl implements BpmnFiringChange {
    private final BpmnMarking consumedMarking;
    private final BpmnMarking producedMarking;
    private final BpmnMarking originalMarking;
    private final BpmnMarking resultMarking;

    public BpmnFiringChangeImpl(BpmnMarking originalMarking, BpmnMarking consumedMarking, BpmnMarking producedMarking, BpmnMarking resultMarking) {
        this.consumedMarking = consumedMarking;
        this.producedMarking = producedMarking;
        this.originalMarking = originalMarking;
        this.resultMarking = resultMarking;
    }

    @Override
    public BpmnMarking getOriginalMarking() {
        return originalMarking;
    }

    public BpmnMarking getConsumedMarking() {
        return consumedMarking;
    }

    public BpmnMarking getProducedMarking() {
        return producedMarking;
    }

    @Override
    public BpmnMarking getResultMarking() {
        return resultMarking;
    }
}
