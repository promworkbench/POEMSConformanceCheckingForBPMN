package org.processmining.sccwbpmnnpos.algorithms.inputs.stochastic_language.stopping;

import org.processmining.sccwbpmnnpos.models.stochastic.language.StochasticLanguage;
import org.processmining.stochasticbpmn.models.stochastic.Probability;

public class ProbabilityMassStochasticLanguageGeneratorStopper implements StochasticLanguageGeneratorStopper {
    private final Probability minRequiredProbability;

    public ProbabilityMassStochasticLanguageGeneratorStopper(Probability minRequiredProbability) {
        this.minRequiredProbability = minRequiredProbability;
    }

    @Override
    public <A> boolean shouldStop(StochasticLanguage<?, ?> stochasticLanguage) {
        return stochasticLanguage.getProbability().compareTo(minRequiredProbability) >= 0;
    }
}
