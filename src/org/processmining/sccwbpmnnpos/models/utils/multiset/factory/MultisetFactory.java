package org.processmining.sccwbpmnnpos.models.utils.multiset.factory;

import org.processmining.sccwbpmnnpos.models.utils.multiset.Multiset;

public interface MultisetFactory {
    <E> Multiset<E> getHashed();

    <E> Multiset<E> getDefault();

    <E> Multiset<E> getDefault(Iterable<E> elements);

    <E> Multiset<E> getHashed(Iterable<E> elements);
}
