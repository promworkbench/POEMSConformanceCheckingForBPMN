package org.processmining.poemsconformancecheckingforbpmn.models.utils.multiset.impl.google;

import org.antlr.v4.runtime.misc.Nullable;
import org.processmining.poemsconformancecheckingforbpmn.models.utils.multiset.Multiset;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GoogleMultisetAdapter<E> implements Multiset<E> {
    private final com.google.common.collect.Multiset<E> elements;

    public GoogleMultisetAdapter(final com.google.common.collect.Multiset<E> elements) {
        this.elements = elements;
    }

    @Override
    public boolean contains(Multiset<E> mSet) {
        for (Entry<E> eEntry : mSet.entrySet()) {
            if (!this.containsAtLeast(eEntry.getElement(), eEntry.getCount())) return false;
        }
        return true;
    }

    @Override
    public boolean isContainedIn(Multiset<E> mSet) {
        return mSet.contains(this);
    }

    @Override
    public boolean containsAtLeast(E element, int count) {
        return elements.count(element) >= count;
    }

    @Override
    public int count(@Nullable Object o) {
        return this.elements.count(o);
    }

    @Override
    public Set<E> elementSet() {
        return elements.elementSet();
    }

    @Override
    public Set<Entry<E>> entrySet() {
        return elements.entrySet().stream().map(e -> new Entry<E>() {
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
    public int add(E element, int count) {
        return elements.add(element, count);
    }

    @Override
    public int remove(Object element, int count) {
        return elements.remove(element, count);
    }

    @Override
    public int setCount(E element, int count) {
        return elements.setCount(element, count);
    }

    @Override
    public boolean setCount(E element, int count, int var3) {
        return elements.setCount(element, count, var3);
    }

    @Override
    public int size() {
        return elements.size();
    }

    @Override
    public boolean isEmpty() {
        return elements.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return elements.contains(o);
    }

    @Override
    public Iterator<E> iterator() {
        return elements.iterator();
    }

    @Override
    public Object[] toArray() {
        return elements.toArray();
    }

    @Override
    public <T> T[] toArray(T[] ts) {
        return elements.toArray(ts);
    }

    @Override
    public List<E> asList() {
        return new ArrayList<>(elements);
    }

    @Override
    public boolean add(E element) {
        return elements.add(element);
    }

    @Override
    public boolean remove(Object element) {
        return elements.remove(element);
    }

    @Override
    public boolean containsAll(Collection<?> elements) {
        return this.elements.containsAll(elements);
    }

    @Override
    public Spliterator<E> spliterator() {
        return elements.spliterator();
    }

    @Override
    public Stream<E> stream() {
        return elements.stream();
    }

    @Override
    public Stream<E> parallelStream() {
        return elements.parallelStream();
    }

    @Override
    public boolean addAll(Collection<? extends E> elements) {
        return this.elements.addAll(elements);
    }

    @Override
    public boolean removeAll(Collection<?> elements) {
        return this.elements.removeAll(elements);
    }

    @Override
    public boolean retainAll(Collection<?> elements) {
        return this.elements.retainAll(elements);
    }

    @Override
    public void clear() {
        elements.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof GoogleMultisetAdapter)) return false;
        GoogleMultisetAdapter<?> that = (GoogleMultisetAdapter<?>) o;
        return Objects.equals(elements, that.elements);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(elements);
    }

    @Override
    public String toString() {
        return elements.toString();
    }

    @Override
    public String toStringNewLines() {
        if (isEmpty()) {
            return toString();
        }
        StringBuilder sb = new StringBuilder();
        for (Entry<E> eEntry : entrySet()) {
            sb.append(eEntry.getElement());
            if (eEntry.getCount() > 1) {
                sb.append(" x ");
                sb.append(eEntry.getCount());
            }
            sb.append("\n");
        }
        return sb.substring(0, sb.length() - 1);
    }
}
