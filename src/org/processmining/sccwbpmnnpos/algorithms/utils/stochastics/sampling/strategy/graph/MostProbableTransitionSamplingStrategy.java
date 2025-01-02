package org.processmining.sccwbpmnnpos.algorithms.utils.stochastics.sampling.strategy.graph;

import org.processmining.sccwbpmnnpos.algorithms.utils.stochastics.sampling.strategy.PriorityQueueStochasticSamplingStrategy;
import org.processmining.sccwbpmnnpos.algorithms.utils.stochastics.sampling.strategy.Sampler;
import org.processmining.sccwbpmnnpos.algorithms.utils.stochastics.sampling.strategy.StochasticSamplingStrategy;
import org.processmining.stochasticbpmn.models.stochastic.StochasticObject;

public class MostProbableTransitionSamplingStrategy<T extends StochasticTransition<? extends StochasticObject, ?
        extends StochasticObject>> implements TransitionSamplingStrategy<T> {
    StochasticSamplingStrategy<T> samplingStrategy = new PriorityQueueStochasticSamplingStrategy<>();

    @Override
    public Sampler<T> getSampler() {
        return samplingStrategy.getSampler();
    }
}
