package org.processmining.sccwbpmnnpos.models.utils.ordered_set.partial;

import org.apache.commons.lang.NotImplementedException;
import org.processmining.sccwbpmnnpos.models.utils.ordered_set.exceptions.PartialOrderLoopNotAllowedException;

import java.util.*;
import java.util.stream.Collectors;

public class MapPartiallyOrderedSet<ELEMENT> implements PartiallyOrderedSet<ELEMENT> {
    private final Map<ELEMENT, Set<ELEMENT>> map;

    public MapPartiallyOrderedSet() {
        this.map = new HashMap<>();
    }

    @Override
    public void setPredecessor(ELEMENT element, ELEMENT predecessor) throws PartialOrderLoopNotAllowedException {
        if (isReachable(element, predecessor)) {
            throw new PartialOrderLoopNotAllowedException(new LinkedList<>());
        }
        map.computeIfAbsent(element, k -> new HashSet<>()).add(element);
    }

    @Override
    public void setPredecessors(ELEMENT element, Collection<ELEMENT> predecessors) throws PartialOrderLoopNotAllowedException {
        for (ELEMENT predecessor : predecessors) {
            if (isReachable(element, predecessor)) {
                throw new PartialOrderLoopNotAllowedException(new LinkedList<>());
            }
        }
        map.computeIfAbsent(element, k -> new HashSet<>()).addAll(predecessors);
    }

    private boolean isReachable(ELEMENT element, ELEMENT origin) {
        Set<ELEMENT> prev = map.getOrDefault(origin, new HashSet<>());
        if (prev.isEmpty()) {
            return false;
        }
        if (prev.contains(element)) {
            return true;
        }
        for (ELEMENT newOrigin : prev) {
            return isReachable(element, newOrigin);
        }
        return false;
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return map.containsKey(o);
    }

    @Override
    public Object[] toArray() {
        return getEntrySet().toArray();
    }

    @Override
    public <T> T[] toArray(T[] ts) {
        return getEntrySet().toArray(ts);
    }

    @Override
    public boolean add(ELEMENT element) {
        return Objects.isNull(map.putIfAbsent(element, new HashSet<>()));
    }

    @Override
    public boolean remove(Object o) {
        return !Objects.isNull(map.remove(o));
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return map.keySet().containsAll(collection);
    }

    @Override
    public boolean addAll(Collection<? extends ELEMENT> collection) {
        boolean added = false;
        for (ELEMENT element : collection) {
            added = added || add(element);
        }
        return added;
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        Set<ELEMENT> alphabet = getAlphabet();
        alphabet.removeAll(collection);
        return removeAll(alphabet);
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        boolean removed = false;
        for (Object element : collection) {
            removed = removed || remove(element);
        }
        return removed;
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Set<ELEMENT> getAlphabet() {
        return map.keySet();
    }

    @Override
    public Set<ELEMENT> getAscendants(ELEMENT element) {
        Set<ELEMENT> ascendants = map.get(element);
        for (ELEMENT ascendant : ascendants) {
            ascendants.addAll(getAscendants(ascendant));
        }
        return ascendants;
    }

    @Override
    public Set<ELEMENT> getPredecessors(ELEMENT element) {
        return map.get(element);
    }

    @Override
    public Set<ELEMENT> getEnabled(Collection<ELEMENT> executed) {
        Set<ELEMENT> alphabet = getAlphabet();
        assert alphabet.containsAll(executed);
        return alphabet.stream().filter(l -> isEnabled(l, executed)).collect(Collectors.toSet());
    }

    @Override
    public boolean isEnabled(ELEMENT element, Collection<ELEMENT> executed) {
        assert map.containsKey(element);
        assert map.keySet().containsAll(executed);
        if (executed.contains(element)) {
            return false;
        }
        Set<ELEMENT> predecessors = map.get(element);
        if (Objects.isNull(predecessors)) {
            return true;
        }
        if (predecessors.isEmpty()) {
            return true;
        }
        for (ELEMENT predecessor : predecessors) {
            if (!executed.contains(predecessor)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Set<Entry<ELEMENT>> getEntrySet() {
        return map.entrySet().stream().map(e -> new PartiallyOrderedSet.Entry<>(e.getKey(), e.getValue())).collect(Collectors.toSet());
    }

    @Override
    public List<ELEMENT> getLoopSequence(ELEMENT element, ELEMENT predecessor) {
        throw new NotImplementedException();
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public Iterator<ELEMENT> iterator() {
        return getAlphabet().iterator();
    }
}
