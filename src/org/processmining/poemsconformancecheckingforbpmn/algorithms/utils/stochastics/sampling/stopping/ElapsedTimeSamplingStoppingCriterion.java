package org.processmining.poemsconformancecheckingforbpmn.algorithms.utils.stochastics.sampling.stopping;

import org.processmining.poemsconformancecheckingforbpmn.models.stochastic.Sample;

public class ElapsedTimeSamplingStoppingCriterion implements SamplingStoppingCriterion {

    private final long maxTime;
    private final long startTime;

    public ElapsedTimeSamplingStoppingCriterion(long maxTime) {
        this.maxTime = maxTime;
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public boolean shouldStop(Sample<?> sample) {
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        return elapsedTime > maxTime;
    }
}
