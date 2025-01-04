package org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.bpmn.statespace;

import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.marking.BpmnMarking;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.path.BpmnPartiallyOrderedPath;

public class BpmnNoOptionToCompleteException extends Exception {
    final BpmnMarking sourceMarking;
    final BpmnMarking reachedMarking;
    final BpmnPartiallyOrderedPath path;

    public BpmnNoOptionToCompleteException(BpmnMarking sourceMarking, BpmnMarking reachedMarking,
                                           BpmnPartiallyOrderedPath path, Throwable cause) {
        super(String.format("Going from marking %s with %s and reaching marking %s results in a state with no option "
                + "to complete", sourceMarking, path, reachedMarking), cause);
        this.sourceMarking = sourceMarking;
        this.reachedMarking = reachedMarking;
        this.path = path;
    }

    public BpmnNoOptionToCompleteException(BpmnMarking sourceMarking, BpmnMarking reachedMarking,
                                           BpmnPartiallyOrderedPath path) {
        super(String.format("Going from marking %s with %s and reaching marking %s results in a state with no option "
                + "to complete", sourceMarking, path, reachedMarking));
        this.sourceMarking = sourceMarking;
        this.reachedMarking = reachedMarking;
        this.path = path;
    }

    public BpmnMarking getSourceMarking() {
        return sourceMarking;
    }

    public BpmnMarking getReachedMarking() {
        return reachedMarking;
    }

    public BpmnPartiallyOrderedPath getPath() {
        return path;
    }
}
