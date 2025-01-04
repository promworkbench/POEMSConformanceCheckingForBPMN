package org.processmining.poemsconformancecheckingforbpmn.models.utils.multiset.utils;

import org.processmining.poemsconformancecheckingforbpmn.models.utils.multiset.Multiset;
import org.processmining.poemsconformancecheckingforbpmn.models.utils.multiset.factory.MultisetFactory;

public interface MultisetUtils {
    static MultisetUtils getInstance() {
        return new SimpleMultisetUtils(MultisetFactory.getInstance());
    }

    <E> Multiset<E> union(Multiset<E> mSet1, Multiset<E> mSet2);

    <E> Multiset<E> difference(Multiset<E> mSet1, Multiset<E> mSet2);

    <E> Multiset<E> sum(Multiset<E> mSet1, Multiset<E> mSet2);

    <E> Multiset<E> intersection(Multiset<E> mSet1, Multiset<E> mSet2);

    <E> Multiset<E> multiply(Multiset<E> m1, int times);

    <E> boolean isSubset(Multiset<E> superSet, Multiset<E> subSet);

    <E> int isContainedTimes(Multiset<E> superSet, Multiset<E> subSet);
}
