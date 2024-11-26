package org.processmining.sccwbpmnnpos.models.stochastic.language;


import org.processmining.stochasticbpmn.models.stochastic.Probability;
import org.processmining.stochasticbpmn.models.stochastic.StochasticObject;

import java.util.Collection;

public class StochasticLanguageEntry<L, E extends Collection<L>> implements Comparable<StochasticLanguageEntry<L, E>>, StochasticObject {
    private final E element;
    private final Probability probability;


    public StochasticLanguageEntry(E element, Probability probability) {
        this.element = element;
        this.probability = probability;
    }

    public E getElement() {
        return element;
    }

    public Probability getProbability() {
        return probability;
    }

    @Override
    public String toString() {
        return probability + ": " + element.toString();
    }

    @Override
    public int compareTo(StochasticLanguageEntry<L, E> tStochasticLanguageEntry) {
        return getProbability().compareTo(tStochasticLanguageEntry.getProbability());
    }
}
