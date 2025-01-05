package org.processmining.poemsconformancecheckingforbpmn.parameters;

import org.processmining.poemsconformancecheckingforbpmn.algorithms.utils.stochastics.sampling.stopping.*;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.utils.stochastics.sampling.stopping.factory.SamplingStoppingCriterionProvider;
import org.processmining.poemsconformancecheckingforbpmn.models.stochastic.Sample;
import org.processmining.stochasticbpmn.models.stochastic.Probability;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class SamplingStoppingCriterionFromParametersProvider implements SamplingStoppingCriterionProvider {
    private final SamplingStoppingCriteriaParameters parameters;
    private final Supplier<Boolean> canceller;

    public SamplingStoppingCriterionFromParametersProvider(
            SamplingStoppingCriteriaParameters parameters,
            Supplier<Boolean> canceller
    ) {
        this.parameters = parameters;
        this.canceller = canceller;
    }

    @Override
    public SamplingStoppingCriterion provide(Probability populationProbability) {
        List<SamplingStoppingCriterion> stoppingCriteria = new LinkedList<>();
        stoppingCriteria.add(sample -> canceller.get());
        for (Map.Entry<SamplingStoppingCriterionType, Object> entry :
                parameters.getSamplingStoppingCriteriaParameters().entrySet()) {
            switch (entry.getKey()) {
                case SAMPLE_SIZE:
                    stoppingCriteria.add(new SampleSizeStoppingCriterion(((SampleSizeParameters) entry.getValue()).getSize()));
                    break;
                case SAMPLE_PROBABILITY:
                    stoppingCriteria.add(new SampleProbabilityMassStoppingCriterion(
                            populationProbability,
                            ((SampleProbabilityPrecisionParameters) entry.getValue()).getPrecision()
                    ));
                    break;
            }
        }
        return new CompositeSamplingStoppingCriterion(stoppingCriteria);
    }
}
