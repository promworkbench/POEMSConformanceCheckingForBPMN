package org.processmining.sccwbpmnnpos.models.utils.ordered_set.partial.eventbased;

import org.processmining.sccwbpmnnpos.models.utils.AlphabetCollection;
import org.processmining.sccwbpmnnpos.models.utils.ordered_set.exceptions.PartialOrderLoopNotAllowedException;
import org.processmining.sccwbpmnnpos.models.utils.ordered_set.partial.PartiallyOrderedSet;

import java.util.Objects;

public interface EventBasedPartiallyOrderedSet<I> extends AlphabetCollection<I> {
    Event<I> fire(I item);

    Event<I> fire(I item, int executionIndex);

    Event<I> fire(Event<I> event);

    int getTimesFired(I item);

    Event<I> getEvent(I item);

    Event<I> getFiringEvent(I item, int executionIndex);

    PartiallyOrderedSet<Event<I>> getPartiallyOrderedSet();

    void connect(Event<I> predecessor, Event<I> successor) throws PartialOrderLoopNotAllowedException;

    void connect(I predecessor, int predecessorIdx, I successor, int successorIdx) throws PartialOrderLoopNotAllowedException;

    void connectLast(I predecessor, I successor) throws PartialOrderLoopNotAllowedException;

    void concatenate(EventBasedPartiallyOrderedSet<I> other) throws PartialOrderLoopNotAllowedException;

    int getNumberOfConnections();

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
//            String s = String.valueOf(getItem().hashCode());
//            String elementIdx = s.substring(Math.max(0, s.length() - 3));
            return getItem().toString() + "." + getFiringIndex();
        }
    }
}
