package org.processmining.sccwbpmnnpos.algorithms.utils.stochastics.graph;

import org.processmining.sccwbpmnnpos.algorithms.utils.stochastics.PriorityQueueStochasticSamplingStrategy;
import org.processmining.sccwbpmnnpos.algorithms.utils.stochastics.Sampler;
import org.processmining.sccwbpmnnpos.algorithms.utils.stochastics.StochasticSamplingStrategy;
import org.processmining.sccwbpmnnpos.algorithms.utils.stochastics.graph.StochasticGraphPathSamplingStrategy.PathOption;
import org.processmining.stochasticbpmn.models.stochastic.StochasticObject;

public class MostProbableStochasticGraphPathSamplingStrategy<T extends PathOption<? extends StochasticObject, ?
        extends StochasticObject>> implements StochasticGraphPathSamplingStrategy<T> {
    StochasticSamplingStrategy<T> samplingStrategy = new PriorityQueueStochasticSamplingStrategy<>();

    @Override
    public Sampler<T> getSampler() {
        return samplingStrategy.getSampler();
    }
}
