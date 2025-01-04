package org.processmining.poemsconformancecheckingforbpmn.algorithms.utils.stochastics.sampling.strategy;

import java.util.Iterator;

public interface Sampler<E> extends Iterator<E>, Iterable<E> {
    void add(E e);

    default Iterator<E> iterator() {
        return this;
    }
}
