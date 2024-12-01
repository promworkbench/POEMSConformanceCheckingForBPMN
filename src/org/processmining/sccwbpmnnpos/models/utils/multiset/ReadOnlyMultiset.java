package org.processmining.sccwbpmnnpos.models.utils.multiset;

import org.antlr.v4.runtime.misc.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface ReadOnlyMultiset<E> extends Iterable<E> {
    int size();

    boolean isEmpty();

    boolean contains(Multiset<E> mSet);

    boolean isContainedIn(Multiset<E> mSet);

    boolean containsAtLeast(E element, int count);

    int count(@Nullable Object o);

    Set<E> elementSet();

    Set<Multiset.Entry<E>> entrySet();

    boolean equals(@Nullable Object other);

    int hashCode();

    String toString();

    boolean contains(@Nullable Object obj);

    boolean containsAll(Collection<?> elements);

    Object[] toArray();

    <T> T[] toArray(T[] var1);

    List<E> asList();

    public interface Entry<E> {
        E getElement();

        int getCount();

        boolean equals(Object other);

        int hashCode();

        String toString();
    }

    String toStringNewLines();
}
