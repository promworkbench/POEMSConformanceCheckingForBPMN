package org.processmining.poemsconformancecheckingforbpmn.algorithms.utils.stochastics.sampling.stopping;

import org.processmining.poemsconformancecheckingforbpmn.models.stochastic.Sample;
import org.processmining.stochasticbpmn.models.stochastic.Probability;

public class SampleProbabilityMassStoppingCriterion implements SamplingStoppingCriterion {
    private final Probability minRequiredProbability;

    public SampleProbabilityMassStoppingCriterion(Probability minRequiredProbability) {
        this.minRequiredProbability = minRequiredProbability;
    }

    public SampleProbabilityMassStoppingCriterion(
            Probability maxAchiavableProbability,
            Probability precision
    ) {
        assert precision.compareTo(Probability.ZERO) > 0;
        assert maxAchiavableProbability.compareTo(precision) > 0;
        this.minRequiredProbability = maxAchiavableProbability.subtract(precision);
    }

    public Probability getMinRequiredProbability() {
        return minRequiredProbability;
    }

    @Override
    public boolean shouldStop(Sample<?> sample) {
        return sample.getProbability().compareTo(minRequiredProbability) >= 0;
    }
}
