package org.processmining.sccwbpmnnpos.models.utils.ordered_set.partial.eventbased;

import org.processmining.sccwbpmnnpos.models.utils.ordered_set.exceptions.PartialOrderLoopNotAllowedException;

public class RepetitiveEventBasedPartiallyOrderedSet<I> extends AbstractEventBasedPartiallyOrderedSet<I> {
    @Override
    public void connect(Event<I> predecessor, Event<I> successor) throws PartialOrderLoopNotAllowedException {
        getPartiallyOrderedSet().setPredecessor(successor, predecessor);
    }
}
