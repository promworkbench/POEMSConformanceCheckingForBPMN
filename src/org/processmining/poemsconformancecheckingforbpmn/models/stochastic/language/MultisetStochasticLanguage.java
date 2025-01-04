package org.processmining.poemsconformancecheckingforbpmn.models.stochastic.language;

import org.processmining.poemsconformancecheckingforbpmn.models.utils.AlphabetCollection;
import org.processmining.poemsconformancecheckingforbpmn.models.utils.multiset.Multiset;
import org.processmining.poemsconformancecheckingforbpmn.models.utils.multiset.ReadOnlyMultiset;
import org.processmining.poemsconformancecheckingforbpmn.models.utils.multiset.factory.MultisetFactory;
import org.processmining.stochasticbpmn.models.stochastic.Probability;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class MultisetStochasticLanguage<L, E extends AlphabetCollection<L>> extends AbstractStochasticLanguage<L, E> {
    private final Multiset<E> multiset;
    private final Probability totalProbabilityMass;
    private final Set<L> alphabet;
    private int totalCount;

    public MultisetStochasticLanguage(Probability totalProbabilityMass) {
        this.multiset = MultisetFactory.getInstance().getDefault();
        this.totalProbabilityMass = totalProbabilityMass;
        this.totalCount = 0;
        this.alphabet = new HashSet<>();
    }

    public void add(E element) {
        multiset.add(element);
        alphabet.addAll(element.getAlphabet());
        totalCount++;
    }

    @Override
    public Set<L> getAlphabet() {
        return alphabet;
    }

    @Override
    public int size() {
        return totalCount;
    }

    @Override
    public Iterator<StochasticLanguageEntry<L, E>> iterator() {
        Iterator<ReadOnlyMultiset.Entry<E>> iterator = multiset.entrySet().iterator();
        return new Iterator<StochasticLanguageEntry<L, E>>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public StochasticLanguageEntry<L, E> next() {
                ReadOnlyMultiset.Entry<E> nextElement = iterator.next();
                BigDecimal count = BigDecimal.valueOf(nextElement.getCount());
                BigDecimal tCount = BigDecimal.valueOf(totalCount);
                Probability probability =
                        Probability.of(count.divide(tCount, 30, RoundingMode.DOWN)).multiply(totalProbabilityMass);
                return new StochasticLanguageEntry<>(nextElement.getElement(), probability);
            }
        };
    }

    @Override
    public Probability getProbability() {
        return totalProbabilityMass;
    }
}
