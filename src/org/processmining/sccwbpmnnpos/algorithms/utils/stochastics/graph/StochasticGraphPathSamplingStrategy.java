package org.processmining.sccwbpmnnpos.algorithms.utils.stochastics.graph;

import org.processmining.sccwbpmnnpos.algorithms.utils.stochastics.StochasticSamplingStrategy;
import org.processmining.sccwbpmnnpos.algorithms.utils.stochastics.graph.StochasticGraphPathSamplingStrategy.PathOption;
import org.processmining.stochasticbpmn.models.stochastic.Probability;
import org.processmining.stochasticbpmn.models.stochastic.StochasticObject;

public interface StochasticGraphPathSamplingStrategy<T extends PathOption<? extends StochasticObject, ? extends StochasticObject>> extends StochasticSamplingStrategy<T> {
    static <T extends PathOption<? extends StochasticObject, ? extends StochasticObject>> StochasticGraphPathSamplingStrategy<T> getInstance(Type type) {
        switch (type) {
            case MOST_PROBABLE:
                return new MostProbableStochasticGraphPathSamplingStrategy<>();
            case SIMPLE_RANDOM:
                return new SimpleRandomStochasticGraphPathSamplingStrategy<>();
            default:
                throw new IllegalArgumentException();
        }
    }

    static <T extends PathOption<? extends StochasticObject, ? extends StochasticObject>> StochasticGraphPathSamplingStrategy<T> getInstance() {
        return getInstance(getDefaultType());
    }

    static Type getDefaultType() {
        return Type.MOST_PROBABLE;
    }

    interface PathOption<S extends StochasticObject, P extends StochasticObject> extends StochasticObject {
        S getState();

        P getPathOption();

        default Probability getProbability() {
            return getState().getProbability().multiply(getPathOption().getProbability());
        }
    }

    public enum Type {
        MOST_PROBABLE,
        SIMPLE_RANDOM;
    }
}
