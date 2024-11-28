package org.processmining.sccwbpmnnpos.algorithms.inputs.stochastic_language.stopping;

import org.processmining.sccwbpmnnpos.models.stochastic.language.StochasticLanguage;

public interface StochasticLanguageGeneratorStopper {
    static StochasticLanguageGeneratorStopper getInstance() {
        return new NumElementsStochasticLanguageGeneratorStopper(1000);
    }

    <A> boolean shouldStop(StochasticLanguage<?, ?> stochasticLanguage);
}
