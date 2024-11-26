package org.processmining.sccwbpmnnpos.models.stochastic.language;


import org.processmining.stochasticbpmn.models.stochastic.Probability;

import java.util.*;
import java.util.stream.Collectors;

public class LinkedListStochasticLanguage<L, E extends Collection<L>> implements StochasticLanguage<L, E> {
    final private List<StochasticLanguageEntry<L, E>> list;
    private Probability totalProbabilityMass;

    public LinkedListStochasticLanguage() {
        this.list = new LinkedList<>();
        this.totalProbabilityMass = Probability.ZERO;
    }

    @Override
    public Set<L> getAlphabet() {
        return list.stream().flatMap(entry -> entry.getElement().stream()).collect(Collectors.toSet());
    }

    public int size() {
        return list.size();
    }

    public void add(E element, Probability probability) {
        list.add(new StochasticLanguageEntry<L, E>(element, probability));
        totalProbabilityMass = totalProbabilityMass.multiply(probability);
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
