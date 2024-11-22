package org.processmining.sccwbpmnnpos.models.utils.ordered_set.partial;

import org.processmining.sccwbpmnnpos.models.utils.ordered_set.OrderedSet;
import org.processmining.sccwbpmnnpos.models.utils.ordered_set.exceptions.PartialOrderLoopNotAllowedException;

import java.util.Collection;
import java.util.Set;

public interface PartiallyOrderedSet<ELEMENT> extends OrderedSet<ELEMENT>, Set<ELEMENT> {
    void setPredecessor(ELEMENT element, ELEMENT predecessor) throws PartialOrderLoopNotAllowedException;

    void setPredecessors(ELEMENT element, Collection<ELEMENT> predecessors) throws PartialOrderLoopNotAllowedException;

    Set<ELEMENT> getAlphabet();

    Set<ELEMENT> getPredecessors(ELEMENT element);

    Set<ELEMENT> getEnabled(Collection<ELEMENT> executed);

    Set<Entry<ELEMENT>> getEntrySet();

    class Entry<E> {
        private final E element;
        private final Set<E> predecessors;

        public Entry(E element, Set<E> predecessors) {
            this.element = element;
            this.predecessors = predecessors;
        }

        public E getElement() {
            return element;
        }

        public Set<E> getPredecessors() {
            return predecessors;
        }
    }
}
