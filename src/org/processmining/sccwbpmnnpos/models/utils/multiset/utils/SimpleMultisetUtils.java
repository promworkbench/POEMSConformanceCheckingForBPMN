package org.processmining.sccwbpmnnpos.models.utils.multiset.utils;

import org.processmining.sccwbpmnnpos.models.utils.multiset.Multiset;
import org.processmining.sccwbpmnnpos.models.utils.multiset.ReadOnlyMultiset;
import org.processmining.sccwbpmnnpos.models.utils.multiset.factory.MultisetFactory;

public class SimpleMultisetUtils extends AbstractMultisetUtils {
    private final MultisetFactory multisetFactory;

    public SimpleMultisetUtils(MultisetFactory multisetFactory) {
        this.multisetFactory = multisetFactory;
    }

    @Override
    public <E> Multiset<E> union(Multiset<E> mSet1, Multiset<E> mSet2) {
        Multiset<E> result = multisetFactory.getDefault(mSet1);
        for (ReadOnlyMultiset.Entry<E> eEntry : mSet2.entrySet()) {
            int count = result.count(eEntry.getElement());
            if (count < eEntry.getCount()) result.setCount(eEntry.getElement(), eEntry.getCount());
        }
        return result;
    }

    @Override
    public <E> Multiset<E> difference(Multiset<E> mSet1, Multiset<E> mSet2) {
        Multiset<E> result = multisetFactory.getDefault(mSet1);
        for (ReadOnlyMultiset.Entry<E> eEntry : mSet2.entrySet()) {
            result.setCount(eEntry.getElement(), Math.max(result.count(eEntry.getElement()) - eEntry.getCount(), 0));
        }
        return result;
    }

    @Override
    public <E> Multiset<E> sum(Multiset<E> mSet1, Multiset<E> mSet2) {
        Multiset<E> result = multisetFactory.getDefault(mSet1);
        result.addAll(mSet2);
        return result;
    }

    @Override
    public <E> Multiset<E> intersection(Multiset<E> mSet1, Multiset<E> mSet2) {
        Multiset<E> result = multisetFactory.getDefault(mSet1);
        for (ReadOnlyMultiset.Entry<E> eEntry : mSet2.entrySet()) {
            int count = mSet1.count(eEntry.getElement());
            result.setCount(eEntry.getElement(), Math.min(eEntry.getCount(), count));
        }
        for (ReadOnlyMultiset.Entry<E> eEntry : mSet1.entrySet()) {
            int count = mSet2.count(eEntry.getElement());
            result.setCount(eEntry.getElement(), Math.min(eEntry.getCount(), count));
        }
        return result;
    }

    @Override
    public <E> Multiset<E> multiply(Multiset<E> m1, int times) {
        Multiset<E> result = multisetFactory.getHashed();
        for (int i = 0; i < times; i++) {
            result.addAll(m1);
        }
        return result;
    }
}
