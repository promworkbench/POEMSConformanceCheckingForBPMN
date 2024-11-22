package org.processmining.sccwbpmnnpos.models.utils.ordered_set.exceptions;

public class PartialOrderLoopNotAllowedException extends Exception {
    private final Object predecessor;
    private final Object successor;

    public PartialOrderLoopNotAllowedException(Object predecessor, Object successor) {
        super(String.format("Making %s a predecessor of %s creates a loop.", predecessor, successor));
        this.predecessor = predecessor;
        this.successor = successor;
    }

    public Object getPredecessor() {
        return predecessor;
    }

    public Object getSuccessor() {
        return successor;
    }
}
