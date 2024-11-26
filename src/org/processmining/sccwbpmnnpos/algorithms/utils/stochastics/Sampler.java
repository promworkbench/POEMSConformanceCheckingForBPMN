package org.processmining.sccwbpmnnpos.algorithms.utils.stochastics;

import java.util.Iterator;

public interface Sampler<E> extends Iterator<E>, Iterable<E> {
    void add(E e);

    default Iterator<E> iterator() {
        return this;
    }
}
