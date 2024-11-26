package org.processmining.sccwbpmnnpos.algorithms.utils.stochastics.graph;

import org.processmining.sccwbpmnnpos.algorithms.utils.stochastics.Sampler;
import org.processmining.sccwbpmnnpos.algorithms.utils.stochastics.graph.StochasticGraphPathSamplingStrategy.PathOption;
import org.processmining.stochasticbpmn.models.stochastic.Probability;
import org.processmining.stochasticbpmn.models.stochastic.StochasticObject;

import java.util.*;

public class RandomStochasticGraphPathSamplingStrategy<T extends PathOption<? extends StochasticObject, ?
        extends StochasticObject>> implements StochasticGraphPathSamplingStrategy<T> {
    @Override
    public Sampler<T> getSampler() {
        final Map<StochasticObject, List<T>> pathOptions = new HashMap<>();
        return new Sampler<T>() {
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
