package org.processmining.sccwbpmnnpos.algorithms.utils.stochastics.sampling.strategy;

import org.processmining.stochasticbpmn.models.stochastic.Probability;
import org.processmining.stochasticbpmn.models.stochastic.StochasticObject;

import java.util.LinkedList;
import java.util.Random;

public class SimpleRandomStochasticSamplingStrategy<E extends StochasticObject> implements SamplingStrategy<E> {
    @Override
    public Sampler<E> getSampler() {
        return new Sampler<E>() {
            private final LinkedList<E> elements = new LinkedList<>();
            private Probability totalProbability = Probability.ZERO;

            @Override
            public void add(E e) {
                elements.add(e);
                totalProbability = totalProbability.add(e.getProbability());
            }

            @Override
            public boolean hasNext() {
                return !elements.isEmpty();
            }

            @Override
            public E next() {
                if (elements.size() == 1) {
                    return elements.pop();
                }
                Random random = new Random();
                Probability randomNumber = Probability.of(random.nextDouble()).multiply(totalProbability);
                Probability reachedProbability = Probability.ZERO;
                int i = 0;
                for (E element : elements) {
                    reachedProbability = reachedProbability.add(element.getProbability());
                    if (reachedProbability.compareTo(randomNumber) >= 0) {
                        elements.remove(i);
                        totalProbability = totalProbability.subtract(element.getProbability());
                        return element;
                    }
                    i++;
                }
                throw new IllegalStateException();
            }
        };
    }
}
