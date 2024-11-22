package org.processmining.sccwbpmnnpos.models.utils.multiset.impl.google;

import com.google.common.collect.Multiset;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ToGoogleMultisetAdapter<E> implements Multiset<E> {
    private final org.processmining.sccwbpmnnpos.models.utils.multiset.Multiset<E> mSet;

    public ToGoogleMultisetAdapter(org.processmining.sccwbpmnnpos.models.utils.multiset.Multiset<E> mSet) {
        this.mSet = mSet;
    }

    @Override
    public int count(Object o) {
        return mSet.count(o);
    }

    @Override
    public int add(E e, int i) {
        return mSet.add(e, i);
    }

    @Override
    public int remove(Object o, int i) {
        return mSet.remove(o, i);
    }

    @Override
    public int setCount(E e, int i) {
        return mSet.setCount(e, i);
    }

    @Override
    public boolean setCount(E e, int i, int i1) {
        return mSet.setCount(e, i, i1);
    }

    @Override
    public Set<E> elementSet() {
        return mSet.elementSet();
    }

    @Override
    public Set<Entry<E>> entrySet() {
        return mSet.entrySet().stream().map(e -> new Entry<E>() {
            @Override
            public E getElement() {
                return e.getElement();
            }

            @Override
            public int getCount() {
                return e.getCount();
            }
        }).collect(Collectors.toSet());
    }

    @Override
    public Iterator<E> iterator() {
        return mSet.iterator();
    }

    @Override
    public Object[] toArray() {
        return mSet.toArray();
    }

    @Override
    public <T> T[] toArray(T[] ts) {
        return mSet.toArray(ts);
    }

    @Override
    public int size() {
        return mSet.size();
    }

    @Override
    public boolean isEmpty() {
        return mSet.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return mSet.contains(o);
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return mSet.containsAll(collection);
    }

    @Override
    public boolean addAll(Collection<? extends E> collection) {
        return mSet.addAll(collection);
    }

    @Override
    public boolean add(E e) {
        return mSet.add(e);
    }

    @Override
    public boolean remove(Object o) {
        return mSet.remove(o);
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        return mSet.removeAll(collection);
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        return mSet.retainAll(collection);
    }

    @Override
    public void clear() {
        mSet.clear();
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ToGoogleMultisetAdapter)) return false;
        ToGoogleMultisetAdapter<?> that = (ToGoogleMultisetAdapter<?>) object;
        return Objects.equals(mSet, that.mSet);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(mSet);
    }

    @Override
    public String toString() {
        return mSet.toString();
    }
}
