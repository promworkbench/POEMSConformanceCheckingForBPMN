package org.processmining.sccwbpmnnpos.models.utils.ordered_set.total;

import java.util.*;

public class ArrayTotallyOrderedSet<ELEMENT> implements TotallyOrderedSet<ELEMENT> {
    private final ArrayList<ELEMENT> elements;

    public ArrayTotallyOrderedSet() {
        this.elements = new ArrayList<>();
    }


    @Override
    public boolean addAll(int i, Collection<? extends ELEMENT> collection) {
        return elements.addAll(i, collection);
    }

    @Override
    public ELEMENT get(int i) {
        return elements.get(i);
    }

    @Override
    public ELEMENT set(int i, ELEMENT element) {
        return elements.set(i, element);
    }

    @Override
    public void add(int i, ELEMENT element) {
        elements.add(i, element);
    }

    @Override
    public ELEMENT remove(int i) {
        return elements.remove(i);
    }

    @Override
    public int indexOf(Object o) {
        return elements.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return elements.lastIndexOf(o);
    }

    @Override
    public ListIterator<ELEMENT> listIterator() {
        return elements.listIterator();
    }

    @Override
    public ListIterator<ELEMENT> listIterator(int i) {
        return elements.listIterator(i);
    }

    @Override
    public List<ELEMENT> subList(int i, int i1) {
        return elements.subList(i, i1);
    }

    @Override
    public Collection<ELEMENT> getAlphabet() {
        return new HashSet<>(elements);
    }

    @Override
    public Collection<ELEMENT> getAscendants(ELEMENT element) {
        int elementIdx = indexOf(element);
        return elements.subList(0, elementIdx - 1);
    }

    public Collection<ELEMENT> getPredecessors(ELEMENT element) {
        int elementIdx = indexOf(element);
        return Collections.singleton(get(elementIdx - 1));
    }

    @Override
    public Collection<ELEMENT> getEnabled(Collection<ELEMENT> executed) {
        int executedSize = executed.size();
        if (executedSize >= elements.size()) {
            return Collections.emptyList();
        }
        ELEMENT nextEnabled = elements.get(executedSize);
        if (!isEnabled(nextEnabled, executed)) {
            return Collections.emptyList();
        }
        return Collections.singletonList(elements.get(executedSize));
    }

    @Override
    public boolean isEnabled(ELEMENT element, Collection<ELEMENT> executed) {
        assert executed.size() < elements.size();
        int idx = 0;
        for (ELEMENT element1 : executed) {
            if (!Objects.equals(element1, elements.get(idx++))) return false;
        }
        return Objects.equals(element, elements.get(idx));
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
    public Iterator<ELEMENT> iterator() {
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
    public boolean add(ELEMENT element) {
        return elements.add(element);
    }

    @Override
    public boolean remove(Object o) {
        return elements.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return elements.containsAll(collection);
    }

    @Override
    public boolean addAll(Collection<? extends ELEMENT> collection) {
        return elements.addAll(collection);
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        return elements.removeAll(collection);
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        return elements.retainAll(collection);
    }

    @Override
    public void clear() {
        elements.clear();
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ArrayTotallyOrderedSet)) return false;
        ArrayTotallyOrderedSet<?> that = (ArrayTotallyOrderedSet<?>) object;
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
}
