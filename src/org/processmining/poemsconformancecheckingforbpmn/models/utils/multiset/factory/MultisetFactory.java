package org.processmining.poemsconformancecheckingforbpmn.models.utils.multiset.factory;

import org.processmining.poemsconformancecheckingforbpmn.models.utils.multiset.Multiset;

public interface MultisetFactory {
    static MultisetFactory getInstance() {
        return new DefaultMultisetFactory();
    }

    <E> Multiset<E> getHashed();

    <E> Multiset<E> getDefault();

    <E> Multiset<E> getFifo();

    <E> Multiset<E> getDefault(Iterable<E> elements);

    <E> Multiset<E> getHashed(Iterable<E> elements);

    <E> Multiset<E> getFifo(Iterable<E> elements);
}
