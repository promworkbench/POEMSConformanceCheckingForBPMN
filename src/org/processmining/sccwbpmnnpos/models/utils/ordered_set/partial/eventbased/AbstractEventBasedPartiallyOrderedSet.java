package org.processmining.sccwbpmnnpos.models.utils.ordered_set.partial.eventbased;

import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import org.processmining.plugins.graphviz.dot.Dot;
import org.processmining.sccwbpmnnpos.models.utils.ordered_set.exceptions.PartialOrderLoopNotAllowedException;
import org.processmining.sccwbpmnnpos.models.utils.ordered_set.partial.ArrayPartiallyOrderedSet;
import org.processmining.sccwbpmnnpos.models.utils.ordered_set.partial.PartiallyOrderedSet;

import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractEventBasedPartiallyOrderedSet<I> implements EventBasedPartiallyOrderedSet<I> {
    private final PartiallyOrderedSet<Event<I>> po;
    private final TObjectIntMap<I> lastFiredIndex;

    public AbstractEventBasedPartiallyOrderedSet() {
        po = new ArrayPartiallyOrderedSet<>();
        lastFiredIndex = new TObjectIntHashMap<>(10, 0.5F, -1);
    }

    @Override
    public Set<I> getAlphabet() {
        return lastFiredIndex.keySet();
    }

    @Override
    public Event<I> fire(I item) {
        int firingIndex = lastFiredIndex.adjustOrPutValue(item, 1, 1);
        Event<I> bpmnEvent = new Event<>(item, firingIndex);
        po.add(bpmnEvent);
        return bpmnEvent;
    }
    @Override
    public Event<I> fire(I item, int executionIndex) {
//        if (lastFiredIndex.get(item) != executionIndex - 1) {
//            return null;
//        }
        if (lastFiredIndex.get(item) == executionIndex) {
            return getFiringEvent(item, executionIndex);
        }
        lastFiredIndex.put(item, executionIndex);
        Event<I> event = new Event<>(item, executionIndex);
        po.add(event);
        return event;
    }

    @Override
    public Event<I> fire(Event<I> event) {
        return fire(event.getItem(), event.getFiringIndex());
    }


    @Override
    public Event<I> getEvent(I item) {
        int timesFired = getTimesFired(item);
        return new Event<>(item, timesFired);
    }

    @Override
    public int getTimesFired(I item) {
        int lastFiringIndex = lastFiredIndex.get(item);
        if (lastFiredIndex.getNoEntryValue() == lastFiringIndex) {
            return 0;
        }
        return lastFiringIndex;
    }

    @Override
    public Event<I> getFiringEvent(I item, int firingIndex) {
        int timesFired = getTimesFired(item);
        if (timesFired < firingIndex) {
            return null;
        }
        return new Event<>(item, firingIndex);
    }

    @Override
    public PartiallyOrderedSet<Event<I>> getPartiallyOrderedSet() {
        return po;
    }

    @Override
    public void connect(I predecessor, int predecessorIdx, I successor, int successorIdx) throws PartialOrderLoopNotAllowedException {
        this.connect(new Event<>(predecessor, predecessorIdx), new Event<>(successor, successorIdx));
    }

    @Override
    public void connectLast(I predecessor, I successor) throws PartialOrderLoopNotAllowedException {
        connect(new Event<>(predecessor, lastFiredIndex.get(predecessor)), new Event<>(successor,
                lastFiredIndex.get(successor)));
    }

    @Override
    public String toString() {
        return po.toString();
    }

    @Override
    public int size() {
        return po.size();
    }

    @Override
    public boolean isEmpty() {
        return po.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return lastFiredIndex.containsKey(o);
    }

    @Override
    public Iterator<I> iterator() {
        Iterator<Event<I>> poIterator = po.iterator();
        return new Iterator<I>() {
            @Override
            public boolean hasNext() {
                return poIterator.hasNext();
            }

            @Override
            public I next() {
                return poIterator.next().getItem();
            }
        };
    }

    @Override
    public Object[] toArray() {
        return po.stream().map(Event::getItem).toArray();
    }

    @Override
    public <T> T[] toArray(T[] ts) {
        return po.stream().map(Event::getItem).collect(Collectors.toList()).toArray(ts);
    }

    @Override
    public boolean add(I item) {
        throw new UnsupportedOperationException("Use fire and connect methods instead");
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return lastFiredIndex.keySet().containsAll(collection);
    }

    @Override
    public boolean addAll(Collection<? extends I> collection) {
        throw new UnsupportedOperationException("Use fire and connect methods instead");
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        po.clear();
        lastFiredIndex.clear();
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof AbstractEventBasedPartiallyOrderedSet)) return false;
        AbstractEventBasedPartiallyOrderedSet<?> that = (AbstractEventBasedPartiallyOrderedSet<?>) object;
        return Objects.equals(po, that.po);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(po);
    }

    @Override
    public void concatenate(EventBasedPartiallyOrderedSet<I> other) throws PartialOrderLoopNotAllowedException {
        PartiallyOrderedSet<Event<I>> otherPO = other.getPartiallyOrderedSet();
        List<Event<I>> resultEvents = otherPO.stream().map(event -> new Event<>(event.getItem(),
                event.getFiringIndex() + getTimesFired(event.getItem()))).collect(Collectors.toList());
        for (Event<I> current : resultEvents) {
            fire(current.getItem(), current.getFiringIndex());
        }
        for (Event<I> current : otherPO) {
            int currentIndex =
                    getTimesFired(current.getItem()) - other.getTimesFired(current.getItem()) + current.getFiringIndex();
            Set<Event<I>> predecessors = otherPO.getPredecessors(current);
            for (Event<I> predecessor : predecessors) {
                int predecessorIndex =
                        getTimesFired(predecessor.getItem()) - other.getTimesFired(predecessor.getItem()) + predecessor.getFiringIndex();
                connect(predecessor.getItem(), predecessorIndex, current.getItem(), currentIndex);
            }
        }
    }

    @Override
    public int getNumberOfConnections() {
        return po.getNumberOfConnections();
    }

    @Override
    public Dot toGraphViz() {
        return po.toGraphViz();
    }
}
