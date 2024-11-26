package org.processmining.sccwbpmnnpos.algorithms.utils.stochastics;

import org.processmining.stochasticbpmn.models.stochastic.StochasticObject;

import java.util.Comparator;
import java.util.PriorityQueue;

public class PriorityQueueStochasticSamplingStrategy<E extends StochasticObject> implements StochasticSamplingStrategy<E> {
    @Override
    public Sampler<E> getSampler() {
        return new Sampler<E>() {
            final PriorityQueue<E> queue = new PriorityQueue<>(Comparator.comparing(StochasticObject::getProbability,
                    Comparator.reverseOrder()));

            @Override
            public void add(E stochasticObject) {
                this.queue.add(stochasticObject);
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
