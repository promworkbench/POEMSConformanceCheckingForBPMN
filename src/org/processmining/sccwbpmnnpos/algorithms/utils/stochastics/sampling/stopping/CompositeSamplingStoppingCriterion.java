package org.processmining.sccwbpmnnpos.algorithms.utils.stochastics.sampling.stopping;

import org.processmining.sccwbpmnnpos.models.stochastic.Sample;

import java.util.Collection;

public class CompositeSamplingStoppingCriterion implements SamplingStoppingCriterion {
    private final Collection<SamplingStoppingCriterion> stoppers;

    public CompositeSamplingStoppingCriterion(Collection<SamplingStoppingCriterion> stoppers) {
        this.stoppers = stoppers;
    }

    @Override
    public boolean shouldStop(Sample<?> sample) {
        for (SamplingStoppingCriterion stopper : stoppers) {
            if (stopper.shouldStop(sample)) {
                return true;
            }
        }
        return false;
    }
}
