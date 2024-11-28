package org.processmining.sccwbpmnnpos.algorithms.utils.stochastics.sampling.strategy;

public interface SamplingStrategy<E> {
    Sampler<E> getSampler();
}
