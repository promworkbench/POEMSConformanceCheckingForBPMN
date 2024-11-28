package org.processmining.sccwbpmnnpos.algorithms.utils.stochastics.sampling.strategy.graph;

import org.processmining.sccwbpmnnpos.algorithms.utils.stochastics.sampling.strategy.PriorityQueueStochasticSamplingStrategy;
import org.processmining.sccwbpmnnpos.algorithms.utils.stochastics.sampling.strategy.Sampler;
import org.processmining.sccwbpmnnpos.algorithms.utils.stochastics.sampling.strategy.StochasticSamplingStrategy;
import org.processmining.sccwbpmnnpos.algorithms.utils.stochastics.sampling.strategy.graph.StochasticGraphPathSamplingStrategy.PathOption;
import org.processmining.stochasticbpmn.models.stochastic.StochasticObject;

public class MostProbableStochasticGraphPathSamplingStrategy<T extends PathOption<? extends StochasticObject, ?
        extends StochasticObject>> implements StochasticGraphPathSamplingStrategy<T> {
    StochasticSamplingStrategy<T> samplingStrategy = new PriorityQueueStochasticSamplingStrategy<>();

    @Override
    public Sampler<T> getSampler() {
        return samplingStrategy.getSampler();
    }
}
