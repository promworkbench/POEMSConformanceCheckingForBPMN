package org.processmining.sccwbpmnnpos.models.utils.multiset.factory;

import com.google.common.collect.ImmutableMultiset;
import org.processmining.sccwbpmnnpos.models.utils.multiset.Multiset;
import org.processmining.sccwbpmnnpos.models.utils.multiset.impl.google.factory.GoogleMultisetFactory;

public class DefaultMultisetFactory implements MultisetFactory {
    private final GoogleMultisetFactory googleMultisetFactory = new GoogleMultisetFactory();

    @Override
    public <E> Multiset<E> getHashed() {
        return googleMultisetFactory.getHashed();
    }

    @Override
    public <E> Multiset<E> getDefault() {
        return getHashed();
    }

    @Override
    public <E> Multiset<E> getFifo() {
        return googleMultisetFactory.getFifo();
    }

    @Override
    public <E> Multiset<E> getDefault(Iterable<E> elements) {
        return googleMultisetFactory.getHashed(elements);
    }

    @Override
    public <E> Multiset<E> getHashed(Iterable<E> elements) {
        return googleMultisetFactory.getHashed(elements);
    }

    @Override
    public <E> Multiset<E> getFifo(Iterable<E> elements) {
        return googleMultisetFactory.getFifo(elements);
    }
}
