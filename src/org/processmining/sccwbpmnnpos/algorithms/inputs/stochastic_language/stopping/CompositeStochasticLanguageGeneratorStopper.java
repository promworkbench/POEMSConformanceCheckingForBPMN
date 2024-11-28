package org.processmining.sccwbpmnnpos.algorithms.inputs.stochastic_language.stopping;

import org.processmining.sccwbpmnnpos.models.stochastic.language.StochasticLanguage;

import java.util.Collection;

public class CompositeStochasticLanguageGeneratorStopper implements StochasticLanguageGeneratorStopper {
    private final Collection<StochasticLanguageGeneratorStopper> stoppers;

    public CompositeStochasticLanguageGeneratorStopper(Collection<StochasticLanguageGeneratorStopper> stoppers) {
        this.stoppers = stoppers;
    }

    @Override
    public <A> boolean shouldStop(StochasticLanguage<?, ?> stochasticLanguage) {
        for (StochasticLanguageGeneratorStopper stopper : stoppers) {
            if (stopper.shouldStop(stochasticLanguage)) {
                return true;
            }
        }
        return false;
    }
}
