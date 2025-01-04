package org.processmining.poemsconformancecheckingforbpmn.algorithms.utils.stochastics.sampling.strategy;

public interface SamplingStrategy<E> {
    Sampler<E> getSampler();
}
