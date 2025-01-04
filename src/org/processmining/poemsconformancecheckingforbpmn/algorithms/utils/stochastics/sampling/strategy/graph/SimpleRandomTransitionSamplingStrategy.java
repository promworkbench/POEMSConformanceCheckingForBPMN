package org.processmining.poemsconformancecheckingforbpmn.algorithms.utils.stochastics.sampling.strategy.graph;

import org.processmining.poemsconformancecheckingforbpmn.algorithms.utils.stochastics.sampling.strategy.Sampler;
import org.processmining.stochasticbpmn.models.stochastic.Probability;
import org.processmining.stochasticbpmn.models.stochastic.StochasticObject;

import java.util.*;

public class SimpleRandomTransitionSamplingStrategy<T extends StochasticTransition<? extends StochasticObject, ?
        extends StochasticObject>> implements TransitionSamplingStrategy<T> {
    @Override
    public Sampler<T> getSampler() {
        return new Sampler<T>() {
            final Map<StochasticObject, LinkedList<T>> pathOptions = new HashMap<>();
            @Override
            public void add(T spPathOption) {
                LinkedList<T> list = pathOptions.computeIfAbsent(spPathOption.getState(), k -> new LinkedList<>());
                list.add(spPathOption);
            }

            @Override
            public boolean hasNext() {
                return !pathOptions.isEmpty();
            }

            @Override
            public T next() {
                StochasticObject nextState = pathOptions.keySet().iterator().next();
                LinkedList<T> paths = pathOptions.get(nextState);
                if (paths.size() == 1) {
                    pathOptions.remove(nextState);
                    return paths.getFirst();
                }
                Probability totalPathsProbability = Probability.ZERO;
                for (T path : paths) {
                    totalPathsProbability = totalPathsProbability.add(path.getProbability());
                }

                Random random = new Random();
                Probability randomNumber = Probability.of(random.nextDouble()).multiply(totalPathsProbability);
                Probability totalProbability = Probability.ZERO;
                int i = 0;
                for (T path : paths) {
                    totalProbability = totalProbability.add(path.getOption().getProbability());
                    if (totalProbability.compareTo(randomNumber) >= 0) {
                        paths.remove(i);
                        return path;
                    }
                    i++;
                }
                if (hasNext()) {
                    return next();
                }
                return null;
            }
        };
    }
}
