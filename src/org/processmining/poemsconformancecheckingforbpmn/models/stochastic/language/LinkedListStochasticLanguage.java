package org.processmining.poemsconformancecheckingforbpmn.models.stochastic.language;


import org.processmining.poemsconformancecheckingforbpmn.models.utils.AlphabetCollection;
import org.processmining.stochasticbpmn.models.stochastic.Probability;

import java.util.*;

public class LinkedListStochasticLanguage<L, E extends AlphabetCollection<L>> extends AbstractStochasticLanguage<L, E> {
    final private List<StochasticLanguageEntry<L, E>> list;
    private final Set<L> alphabet;
    private Probability totalProbabilityMass;

    public LinkedListStochasticLanguage() {
        this.list = new LinkedList<>();
        this.totalProbabilityMass = Probability.ZERO;
        this.alphabet = new HashSet<>();
    }

    @Override
    public Set<L> getAlphabet() {
        return alphabet;
    }

    @Override
    public int size() {
        return list.size();
    }

    public void add(E element, Probability probability) {
        list.add(new StochasticLanguageEntry<L, E>(element, probability));
        totalProbabilityMass = totalProbabilityMass.add(probability);
        alphabet.addAll(element.getAlphabet());
    }

    @Override
    public Iterator<StochasticLanguageEntry<L, E>> iterator() {
        return list.iterator();
    }

    @Override
    public String toString() {
        return list.toString();
    }

    @Override
    public Probability getProbability() {
        return totalProbabilityMass;
    }
}
