package org.processmining.sccwbpmnnpos.models.utils.ordered_set.partial.eventbased;

import java.util.Objects;

public class PartialOrderFiringEvent<I> {
    private final I item;
    private final int firingIndex;

    public PartialOrderFiringEvent(I item, int firingIndex) {
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
        if (!(object instanceof PartialOrderFiringEvent)) return false;
        PartialOrderFiringEvent<?> that = (PartialOrderFiringEvent<?>) object;
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