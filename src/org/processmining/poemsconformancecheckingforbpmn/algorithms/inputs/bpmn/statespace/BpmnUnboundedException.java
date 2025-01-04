package org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.bpmn.statespace;

import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.marking.BpmnMarking;

public class BpmnUnboundedException extends Exception {
    private final BpmnMarking reachedMarking;
    private final BpmnMarking previousSubMarking;

    public BpmnUnboundedException(BpmnMarking reachedMarking, BpmnMarking previousSubMarking) {
        super(String.format("%s reached from %s indicating, this model is unbounded.", reachedMarking,
                previousSubMarking));
        this.reachedMarking = reachedMarking;
        this.previousSubMarking = previousSubMarking;
    }

    public BpmnMarking getReachedMarking() {
        return reachedMarking;
    }

    public BpmnMarking getPreviousSubMarking() {
        return previousSubMarking;
    }
}
