package org.processmining.sccwbpmnnpos.models.utils.multiset.utils;

import org.processmining.sccwbpmnnpos.models.utils.multiset.Multiset;
import org.processmining.sccwbpmnnpos.models.utils.multiset.ReadOnlyMultiset;

public abstract class AbstractMultisetUtils implements MultisetUtils {
    @Override
    public <E> boolean isSubset(Multiset<E> superSet, Multiset<E> subSet) {
        for (ReadOnlyMultiset.Entry<E> eEntry : subSet.entrySet()) {
            if (!superSet.containsAtLeast(eEntry.getElement(), eEntry.getCount())) return false;
        }
        return true;
    }

    @Override
    public <E> int isContainedTimes(Multiset<E> superSet, Multiset<E> subSet) {
        int maxTimesContained = Integer.MAX_VALUE;
        for (ReadOnlyMultiset.Entry<E> eEntry : subSet.entrySet()) {
            int superCount = superSet.count(eEntry.getElement());
            int timesContained = superCount / eEntry.getCount();
            if (timesContained < maxTimesContained) maxTimesContained = timesContained;
        }
        return maxTimesContained;
    }
}
