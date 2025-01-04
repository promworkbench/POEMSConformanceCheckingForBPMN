package org.processmining.poemsconformancecheckingforbpmn.models.utils.multiset;

import org.antlr.v4.runtime.misc.Nullable;

import java.util.Collection;

public interface Multiset<E> extends Collection<E>, ReadOnlyMultiset<E> {
    int add(@Nullable E element, int count);

    int remove(@Nullable Object o, int count);

    int setCount(E element, int count);

    boolean setCount(E element, int count, int var3);

    boolean add(E element);

    boolean remove(@Nullable Object element);

    boolean removeAll(Collection<?> elements);

    boolean retainAll(Collection<?> elements);
}
