package org.processmining.sccwbpmnnpos.algorithms.utils.stochastics;

import org.processmining.stochasticbpmn.models.stochastic.StochasticObject;

public class SamplingStrategyStochasticDecorator<E extends StochasticObject> implements StochasticSamplingStrategy<E> {
    private final SamplingStrategy<E> samplingStrategy;

    public SamplingStrategyStochasticDecorator(SamplingStrategy<E> samplingStrategy) {
        this.samplingStrategy = samplingStrategy;
    }

    @Override
    public Sampler<E> getSampler() {
        return samplingStrategy.getSampler();
    }
}
