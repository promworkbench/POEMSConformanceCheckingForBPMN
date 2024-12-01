package org.processmining.sccwbpmnnpos.models.utils.ordered_set.partial.eventbased;

import org.processmining.sccwbpmnnpos.models.utils.ordered_set.exceptions.PartialOrderLoopNotAllowedException;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class NonRepetitiveEventBasedPartiallyOrderedSet<I> extends AbstractEventBasedPartiallyOrderedSet<I> {
    protected boolean canConnect(Event<I> predecessor, I successor) {
        if (Objects.equals(predecessor.getItem(), successor)) {
            return false;
        }
        Set<Event<I>> predecessors = getPartiallyOrderedSet().getAscendants(predecessor);
        for (Event<I> poPredecessor : predecessors) {
            if (poPredecessor.getFiringIndex() == 0) {
                continue;
            }
            if (poPredecessor.getItem().equals(successor)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void connect(Event<I> predecessor, Event<I> successor) throws PartialOrderLoopNotAllowedException {
        if (!canConnect(predecessor, successor.getItem())) {
            List<Event<I>> loopSequence = getPartiallyOrderedSet().getLoopSequence(new Event<>(successor.getItem(),
                    successor.getFiringIndex() - 1), predecessor);
            loopSequence.remove(loopSequence.size() - 1);
            loopSequence.add(successor);
            throw new PartialOrderLoopNotAllowedException(loopSequence);
        }
        getPartiallyOrderedSet().setPredecessor(successor, predecessor);
    }
}
