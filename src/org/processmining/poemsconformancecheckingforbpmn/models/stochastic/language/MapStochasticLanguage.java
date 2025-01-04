package org.processmining.poemsconformancecheckingforbpmn.models.stochastic.language;

import org.processmining.poemsconformancecheckingforbpmn.models.utils.AlphabetCollection;
import org.processmining.stochasticbpmn.models.stochastic.Probability;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class MapStochasticLanguage<L, E extends AlphabetCollection<L>> extends AbstractStochasticLanguage<L, E> {
    private final Map<E, StochasticLanguageEntry<L, E>> languageMap;
    private final Set<L> alphabet;
    private Probability totalProbabilityMass;

    public MapStochasticLanguage(Map<E, StochasticLanguageEntry<L, E>> languageMap) {
        this.languageMap = languageMap;
        this.totalProbabilityMass = Probability.ZERO;
        this.alphabet = new HashSet<>();
    }

    public void add(E element, Probability probability) {
        StochasticLanguageEntry<L, E> previous = languageMap.putIfAbsent(element,
                new StochasticLanguageEntry<>(element, probability));
        if (previous != null) {
            languageMap.put(element, new StochasticLanguageEntry<>(element,
                    probability.add(previous.getProbability())));
        } else {
            alphabet.addAll(element.getAlphabet());
        }
        totalProbabilityMass = totalProbabilityMass.add(probability);
    }

    @Override
    public Set<L> getAlphabet() {
        return alphabet;
    }

    @Override
    public int size() {
        return languageMap.size();
    }

    @Override
    public Iterator<StochasticLanguageEntry<L, E>> iterator() {
        return languageMap.values().iterator();
    }

    @Override
    public Probability getProbability() {
        return totalProbabilityMass;
    }
}
