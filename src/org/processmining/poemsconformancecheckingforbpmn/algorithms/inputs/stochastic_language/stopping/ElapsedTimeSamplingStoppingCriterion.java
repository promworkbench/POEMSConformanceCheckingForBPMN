package org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.stochastic_language.stopping;

import org.processmining.poemsconformancecheckingforbpmn.algorithms.utils.stochastics.sampling.stopping.SamplingStoppingCriterion;
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
