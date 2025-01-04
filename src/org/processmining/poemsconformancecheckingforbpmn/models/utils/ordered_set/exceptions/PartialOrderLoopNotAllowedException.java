package org.processmining.poemsconformancecheckingforbpmn.models.utils.ordered_set.exceptions;

import java.util.List;

public class PartialOrderLoopNotAllowedException extends Exception {
    private final List<? extends Object> loopSequence;

    public PartialOrderLoopNotAllowedException(final List<? extends Object> loopSequence) {
        super(String.format("Loop sequence: %s", loopSequence));
        this.loopSequence = loopSequence;
    }

    public List<? extends Object> getLoopSequence() {
        return loopSequence;
    }
}
