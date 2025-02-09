package org.processmining.poemsconformancecheckingforbpmn.algorithms.utils.stochastics.sampling.strategy.transition;

import org.processmining.stochasticbpmn.models.stochastic.Probability;
import org.processmining.stochasticbpmn.models.stochastic.StochasticObject;

public interface StochasticTransition<S extends StochasticObject, P extends StochasticObject> extends StochasticObject {
    S getState();

    P getOption();

    default Probability getProbability() {
        return getState().getProbability().multiply(getOption().getProbability());
    }
}
