package org.processmining.sccwbpmnnpos.algorithms.utils.stochastics.sampling.strategy.graph;

import org.processmining.sccwbpmnnpos.algorithms.utils.stochastics.sampling.strategy.Sampler;
import org.processmining.sccwbpmnnpos.algorithms.utils.stochastics.sampling.strategy.graph.StochasticGraphPathSamplingStrategy.PathOption;
import org.processmining.stochasticbpmn.models.stochastic.Probability;
import org.processmining.stochasticbpmn.models.stochastic.StochasticObject;

import java.util.*;

public class SimpleRandomStochasticGraphPathSamplingStrategy<T extends PathOption<? extends StochasticObject, ?
        extends StochasticObject>> implements StochasticGraphPathSamplingStrategy<T> {
    @Override
    public Sampler<T> getSampler() {
        return new Sampler<T>() {
            final Map<StochasticObject, List<T>> pathOptions = new HashMap<>();
            @Override
            public void add(T spPathOption) {
                List<T> list = pathOptions.computeIfAbsent(spPathOption.getState(), k -> new LinkedList<>());
                list.add(spPathOption);
            }

            @Override
            public boolean hasNext() {
                return !pathOptions.isEmpty();
            }

            @Override
            public T next() {
                StochasticObject nextState = pathOptions.keySet().iterator().next();
                List<T> paths = pathOptions.get(nextState);
                pathOptions.remove(nextState);
                Random random = new Random();
                Probability randomNumber = Probability.of(random.nextDouble());
                Probability totalProbability = Probability.ZERO;
                for (T path : paths) {
                    totalProbability = totalProbability.add(path.getPathOption().getProbability());
                    if (totalProbability.compareTo(randomNumber) > 0) {
                        return path;
                    }
                }
                if (hasNext()) {
                    return next();
                }
                return null;
            }
        };
    }
}