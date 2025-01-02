package org.processmining.sccwbpmnnpos.algorithms.utils.stochastics.sampling.stopping;

import org.processmining.sccwbpmnnpos.models.stochastic.Sample;

public interface SamplingStoppingCriterion {
    static SamplingStoppingCriterion getInstance() {
        return new SampleSizeStoppingCriterion(1000L);
    }

    boolean shouldStop(Sample<?> sample);
}
