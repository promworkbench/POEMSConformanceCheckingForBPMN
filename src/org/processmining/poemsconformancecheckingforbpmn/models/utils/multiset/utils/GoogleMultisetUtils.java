package org.processmining.poemsconformancecheckingforbpmn.models.utils.multiset.utils;

import com.google.common.collect.Multisets;
import org.processmining.poemsconformancecheckingforbpmn.models.utils.multiset.Multiset;
import org.processmining.poemsconformancecheckingforbpmn.models.utils.multiset.impl.google.factory.GoogleMultisetFactory;

public class GoogleMultisetUtils extends AbstractMultisetUtils {
    private final GoogleMultisetFactory factory;

    public GoogleMultisetUtils(GoogleMultisetFactory factory) {
        this.factory = factory;
    }

    @Override
    public <E> Multiset<E> union(Multiset<E> mSet1, Multiset<E> mSet2) {
        return factory.adapt(Multisets.union(factory.adapt(mSet1), factory.adapt(mSet2)));
    }

    @Override
    public <E> Multiset<E> difference(Multiset<E> mSet1, Multiset<E> mSet2) {
        return factory.adapt(Multisets.difference(factory.adapt(mSet1), factory.adapt(mSet2)));
    }

    @Override
    public <E> Multiset<E> sum(Multiset<E> mSet1, Multiset<E> mSet2) {
        return factory.adapt(Multisets.sum(factory.adapt(mSet1), factory.adapt(mSet2)));
    }

    @Override
    public <E> Multiset<E> intersection(Multiset<E> mSet1, Multiset<E> mSet2) {
        return factory.adapt(Multisets.intersection(factory.adapt(mSet1), factory.adapt(mSet2)));
    }

    @Override
    public <E> Multiset<E> multiply(Multiset<E> m1, int times) {
        Multiset<E> result = m1;
        for (int i = 0; i < times - 1; i++) {
            result = sum(result, result);
        }
        return result;
    }
}
