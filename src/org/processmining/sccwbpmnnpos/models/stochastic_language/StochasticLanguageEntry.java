package org.processmining.sccwbpmnnpos.models.stochastic_language;

import org.processmining.sccwbpmnnpos.models.utils.ordered_set.OrderedSet;

public class StochasticLanguageEntry<T extends OrderedSet<?>> implements Comparable<StochasticLanguageEntry<T>> {
    private final T element;
    private final Double probability;


    public StochasticLanguageEntry(T element, Double probability) {
        this.element = element;
        this.probability = probability;
    }

    public T getElement() {
        return element;
    }

    public Double getProbability() {
        return probability;
    }

    @Override
    public String toString() {
        return probability + ": " + element.toString();
    }

    @Override
    public int compareTo(StochasticLanguageEntry<T> tStochasticLanguageEntry) {
        return getProbability().compareTo(tStochasticLanguageEntry.getProbability());
    }
}
