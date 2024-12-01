package org.processmining.sccwbpmnnpos.algorithms.inputs.stochastic_language.stopping;

import org.processmining.sccwbpmnnpos.models.stochastic.language.StochasticLanguage;

public class NumElementsStochasticLanguageGeneratorStopper implements StochasticLanguageGeneratorStopper {
    private final int numElementsRequired;

    public NumElementsStochasticLanguageGeneratorStopper(int numElementsRequired) {
        this.numElementsRequired = numElementsRequired;
    }

    @Override
    public <A> boolean shouldStop(StochasticLanguage<?, ?> stochasticLanguage) {
        return stochasticLanguage.size() >= numElementsRequired;
    }
}
