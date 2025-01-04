package org.processmining.poemsconformancecheckingforbpmn.models.utils.multiset.impl.google.factory;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.LinkedHashMultiset;
import org.processmining.poemsconformancecheckingforbpmn.models.utils.multiset.Multiset;
import org.processmining.poemsconformancecheckingforbpmn.models.utils.multiset.factory.MultisetFactory;
import org.processmining.poemsconformancecheckingforbpmn.models.utils.multiset.impl.google.GoogleMultisetAdapter;
import org.processmining.poemsconformancecheckingforbpmn.models.utils.multiset.impl.google.ToGoogleMultisetAdapter;

public class GoogleMultisetFactory implements MultisetFactory {
    @Override
    public <E> Multiset<E> getHashed() {
        return adapt(HashMultiset.create());
    }

    public <E> Multiset<E> getOrdered() {
        return adapt(LinkedHashMultiset.create());
    }

    @Override
    public <E> Multiset<E> getDefault() {
        return getHashed();
    }

    @Override
    public <E> Multiset<E> getFifo() {
        return adapt(LinkedHashMultiset.create());
    }

    @Override
    public <E> Multiset<E> getDefault(Iterable<E> elements) {
        return getHashed(elements);
    }

    @Override
    public <E> Multiset<E> getHashed(Iterable<E> elements) {
        return adapt(HashMultiset.create(elements));
    }

    @Override
    public <E> Multiset<E> getFifo(Iterable<E> elements) {
        return adapt(LinkedHashMultiset.create(elements));
    }

    public <E> Multiset<E> adapt(com.google.common.collect.Multiset<E> mSet) {
        return new GoogleMultisetAdapter<>(mSet);
    }

    public <E> com.google.common.collect.Multiset<E> adapt(Multiset<E> mSet) {
        return new ToGoogleMultisetAdapter<>(mSet);
    }
}
