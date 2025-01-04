package org.processmining.poemsconformancecheckingforbpmn.algorithms.utils.stochastics.sampling.stopping;

import org.processmining.poemsconformancecheckingforbpmn.models.stochastic.Sample;

public interface SamplingStoppingCriterion {
    static SamplingStoppingCriterion getInstance() {
        return new SampleSizeStoppingCriterion(1000L);
    }

    boolean shouldStop(Sample<?> sample);
}
