package org.processmining.sccwbpmnnpos.algorithms.utils.stochastics.sampling.strategy;

import java.util.LinkedList;
import java.util.Queue;

public class FIFOSamplingStrategy<E> implements SamplingStrategy<E> {
    @Override
    public Sampler<E> getSampler() {
        return new Sampler<E>() {
            final Queue<E> queue = new LinkedList<>();

            @Override
            public void add(E e) {
                queue.add(e);
            }

            @Override
            public boolean hasNext() {
                return !queue.isEmpty();
            }

            @Override
            public E next() {
                return queue.poll();
            }
        };
    }
}
