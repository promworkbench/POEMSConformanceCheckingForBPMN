package org.processmining.poemsconformancecheckingforbpmn.algorithms.utils.stochastics.sampling.stopping;

import org.processmining.poemsconformancecheckingforbpmn.models.stochastic.Sample;

public class SampleSizeStoppingCriterion implements SamplingStoppingCriterion {
    private final long size;

    public SampleSizeStoppingCriterion(long size) {
        this.size = size;
    }

    public long getSize() {
        return size;
    }

    @Override
    public boolean shouldStop(Sample<?> sample) {
        return sample.size() >= size;
    }
}
