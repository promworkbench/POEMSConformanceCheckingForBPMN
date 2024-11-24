package org.processmining.sccwbpmnnpos.algorithms.inputs.model.statespace;

import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.BpmnMarking;

public class BpmnUnboundedException extends Exception {
    final BpmnMarking sourceMarking;
    final BpmnMarking targetMarking;

    public BpmnUnboundedException(BpmnMarking sourceMarking, BpmnMarking targetMarking, Throwable cause) {
        super(String.format("Going from marking %s to marking %s indicates that this model is unbounded",
                sourceMarking, targetMarking), cause);
        this.sourceMarking = sourceMarking;
        this.targetMarking = targetMarking;
    }

    public BpmnUnboundedException(BpmnMarking sourceMarking, BpmnMarking targetMarking) {
        super(String.format("Going from marking %s to marking %s indicates that this model is unbounded",
                sourceMarking, targetMarking));
        this.sourceMarking = sourceMarking;
        this.targetMarking = targetMarking;
    }
}
