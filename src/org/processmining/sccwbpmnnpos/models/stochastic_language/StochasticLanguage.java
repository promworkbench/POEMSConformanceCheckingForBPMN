package org.processmining.sccwbpmnnpos.models.stochastic_language;

import org.processmining.sccwbpmnnpos.models.utils.ordered_set.OrderedSet;

import java.util.Collection;

public interface StochasticLanguage<L, T extends OrderedSet<L>> extends Iterable<T> {
    Collection<L> getAlphabet();

    int size();

    Collection<StochasticLanguageEntry<T>> entries();

    /**
     * Set an element with its probability.
     * Note: it will override existing element if it already exists.
     *
     * @param element
     * @param probability
     */
    void put(final T element, final Double probability);
}
