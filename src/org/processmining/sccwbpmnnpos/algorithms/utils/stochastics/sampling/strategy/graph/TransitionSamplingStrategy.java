package org.processmining.sccwbpmnnpos.algorithms.utils.stochastics.sampling.strategy.graph;

import org.processmining.sccwbpmnnpos.algorithms.utils.stochastics.sampling.strategy.StochasticSamplingStrategy;
import org.processmining.stochasticbpmn.models.stochastic.StochasticObject;

public interface TransitionSamplingStrategy<T extends StochasticTransition<? extends StochasticObject, ? extends StochasticObject>> extends StochasticSamplingStrategy<T> {
    static <T extends StochasticTransition<? extends StochasticObject, ? extends StochasticObject>> TransitionSamplingStrategy<T> getInstance(TansitionSamplingStrategyType type) {
        switch (type) {
            case MOST_PROBABLE:
                return new MostProbableTransitionSamplingStrategy<>();
            case SIMPLE_RANDOM:
                return new SimpleRandomTransitionSamplingStrategy<>();
            default:
                throw new IllegalArgumentException();
        }
    }

    static <T extends StochasticTransition<? extends StochasticObject, ? extends StochasticObject>> TransitionSamplingStrategy<T> getInstance() {
        return getInstance(getDefaultType());
    }

    static TansitionSamplingStrategyType getDefaultType() {
        return TansitionSamplingStrategyType.MOST_PROBABLE;
    }

}
