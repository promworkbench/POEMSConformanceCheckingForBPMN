package org.processmining.sccwbpmnnpos.models.utils.ordered_set.partial.eventbased;

import org.processmining.sccwbpmnnpos.models.utils.ordered_set.exceptions.PartialOrderLoopNotAllowedException;
import org.processmining.sccwbpmnnpos.models.utils.ordered_set.partial.PartiallyOrderedSet;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

public interface EventBasedPartiallyOrderedSet<I> extends Collection<I> {
    Set<I> getAlphabet();

    Event<I> fire(I item);

    int getTimesFired(I item);

    Event<I> getEvent(I item);

    Event<I> getFiringEvent(I item, int executionIndex);

    PartiallyOrderedSet<Event<I>> getPartiallyOrderedSet();

    void connect(Event<I> predecessor, Event<I> successor) throws PartialOrderLoopNotAllowedException;

    void connect(I predecessor, int predecessorIdx, I successor, int successorIdx) throws PartialOrderLoopNotAllowedException;;

    void connectLast(I predecessor, I successor) throws PartialOrderLoopNotAllowedException;

    void concatenate(EventBasedPartiallyOrderedSet<I> other) throws PartialOrderLoopNotAllowedException;

    public class Event<I> {
        private final I item;
        private final int firingIndex;

        public Event(I item, int firingIndex) {
            this.item = item;
            this.firingIndex = firingIndex;
        }

        public I getItem() {
            return item;
        }

        public int getFiringIndex() {
            return firingIndex;
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) return true;
            if (!(object instanceof EventBasedPartiallyOrderedSet.Event)) return false;
            Event<?> that = (Event<?>) object;
            return firingIndex == that.firingIndex && Objects.equals(item, that.item);
        }

        @Override
        public int hashCode() {
            return Objects.hash(item, firingIndex);
        }

        @Override
        public String toString() {
            return getItem() + "." + getFiringIndex();
        }
    }
}