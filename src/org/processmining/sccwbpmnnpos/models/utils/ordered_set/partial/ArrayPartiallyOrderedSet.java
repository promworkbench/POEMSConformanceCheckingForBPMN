package org.processmining.sccwbpmnnpos.models.utils.ordered_set.partial;

import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import org.processmining.sccwbpmnnpos.models.utils.ordered_set.exceptions.PartialOrderLoopNotAllowedException;

import java.util.*;
import java.util.stream.Collectors;

public class ArrayPartiallyOrderedSet<ELEMENT> implements PartiallyOrderedSet<ELEMENT> {
    private final TObjectIntMap<ELEMENT> elementIndex;
    private final ArrayList<ELEMENT> elements;
    private final ArrayList<Set<Integer>> predecessors;

    public ArrayPartiallyOrderedSet() {
        this.elementIndex = new TObjectIntHashMap<>(10, 0.5F, -1);
        this.elements = new ArrayList<>();
        this.predecessors = new ArrayList<>();
    }

    private int addNew(ELEMENT element) {
        int next = elements.size();
        elementIndex.put(element, next);
        elements.add(element);
        predecessors.add(new HashSet<>());
        return next;
    }

    protected int putIfAbsent(ELEMENT element) {
        boolean contained = elementIndex.containsKey(element);
        if (contained) {
            return elementIndex.get(element);
        }
        return addNew(element);
    }

    protected Set<Integer> putAllIfAbsent(Collection<ELEMENT> elementCollection) {
        return elementCollection.stream().map(this::putIfAbsent).collect(Collectors.toSet());
    }

    @Override
    public void setPredecessor(ELEMENT element, ELEMENT predecessor) throws PartialOrderLoopNotAllowedException {
        int predIdx = putIfAbsent(predecessor);
        int idx = putIfAbsent(element);
        setPredecessor(idx, predIdx);
    }

    private List<Integer> getLoopSequenceRecursive(int elementIdx, int predecessorIdx) {
        if (elementIdx == predecessorIdx) {
            LinkedList<Integer> loop = new LinkedList<>();
            loop.add(predecessorIdx);
            return loop;
        }
        Set<Integer> currentPredecessors = predecessors.get(predecessorIdx);
        for (Integer currentPredecessor : currentPredecessors) {
            List<Integer> loop = getLoopSequenceRecursive(elementIdx, currentPredecessor);
            if (loop == null) continue;
            loop.add(predecessorIdx);
            return loop;
        }
        return null;
    }

    private List<Integer> getLoopSequence(int elementIdx, int predecessorIdx) {
        List<Integer> loopSequence = getLoopSequenceRecursive(elementIdx, predecessorIdx);
        if (Objects.isNull(loopSequence)) {
            return null;
        }
        loopSequence.add(elementIdx);
        return loopSequence;
    }

    public List<ELEMENT> getLoopSequence(ELEMENT element, ELEMENT predecessor) {
        List<Integer> loopSequence = getLoopSequence(getIndex(element), getIndex(predecessor));
        if (Objects.isNull(loopSequence)) {
            return null;
        }
        return loopSequence.stream().map(this::getElement).collect(Collectors.toList());

    }

    private void setPredecessor(int elementIdx, int predecessorIdx) throws PartialOrderLoopNotAllowedException {
        if (elementIdx < predecessorIdx) {
            List<Integer> loopSequence = getLoopSequence(elementIdx, predecessorIdx);
            if (Objects.nonNull(loopSequence)) {
                throw new PartialOrderLoopNotAllowedException(loopSequence.stream().map(this::getElement).collect
                        (Collectors.toList()));
            } else {
                throw new PartialOrderLoopNotAllowedException(Arrays.asList(getElement(elementIdx),
                        getElement(predecessorIdx), getElement(elementIdx)));
            }

        }
        predecessors.get(elementIdx).add(predecessorIdx);
    }

    private void setPredecessors(int elementIdx, Collection<Integer> predecessorIdx) throws PartialOrderLoopNotAllowedException {
        for (Integer idx : predecessorIdx) {
            setPredecessor(elementIdx, idx);
        }
    }

    @Override
    public void setPredecessors(ELEMENT element, Collection<ELEMENT> predecessors) throws PartialOrderLoopNotAllowedException {
        Collection<Integer> predecessorIdx = putAllIfAbsent(predecessors);
        int elementIdx = putIfAbsent(element);
        setPredecessors(elementIdx, predecessorIdx);
    }

    @Override
    public Set<ELEMENT> getAlphabet() {
        return elementIndex.keySet();
    }

    public ELEMENT getElement(int idx) {
        if (idx >= elements.size()) {
            return null;
        }
        return elements.get(idx);
    }

    public Integer getIndex(ELEMENT element) {
        int idx = elementIndex.get(element);
        if (idx == elementIndex.getNoEntryValue()) {
            return null;
        }
        return idx;
    }

    protected Set<Integer> toIndex(Collection<ELEMENT> elementCollection) {
        return elementCollection.stream().map(this::getIndex).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    protected Set<ELEMENT> toElements(Collection<Integer> elementCollection) {
        return elementCollection.stream().map(elements::get).collect(Collectors.toSet());
    }

    private Set<Integer> getAscendants(int idx) {
        Set<Integer> predecessorsIdx = predecessors.get(idx);
        if (Objects.isNull(predecessorsIdx) || predecessorsIdx.isEmpty()) {
            return Collections.emptySet();
        }
        Set<Integer> result = new HashSet<>(predecessorsIdx);
        for (Integer predecessor : predecessorsIdx) {
            result.addAll(getAscendants(predecessor));
        }
        return result;
    }

    @Override
    public Set<ELEMENT> getAscendants(ELEMENT element) {
        Integer idx = getIndex(element);
        if (Objects.isNull(idx)) {
            return Collections.emptySet();
        }
        Set<Integer> predecessorsIdx = getAscendants(idx);
        return toElements(predecessorsIdx);
    }

    public Set<ELEMENT> getPredecessors(ELEMENT element) {
        Integer idx = getIndex(element);
        if (Objects.isNull(idx)) {
            return Collections.emptySet();
        }
        return toElements(predecessors.get(idx));
    }

    @Override
    public Set<ELEMENT> getEnabled(Collection<ELEMENT> executed) {
        Set<Integer> executedIdx = toIndex(executed);
        final Set<ELEMENT> result = new HashSet<>();
        int idx = 0;
        for (ELEMENT element : elements) {
            if (!isEnabled(idx++, executedIdx)) continue;
            result.add(element);
        }
        return result;
    }

    protected boolean isEnabled(int idx, Set<Integer> executedIdx) {
        if (executedIdx.contains(idx)) {
            return false;
        }
        Set<Integer> predecessors = this.predecessors.get(idx);
        if (Objects.isNull(predecessors) || predecessors.isEmpty()) {
            return true;
        }
        return executedIdx.containsAll(predecessors);
    }

    @Override
    public boolean isEnabled(ELEMENT element, Collection<ELEMENT> executed) {
        assert elementIndex.containsKey(element);

        int idx = getIndex(element);
        Set<Integer> executedIdx = toIndex(executed);

        return isEnabled(idx, executedIdx);
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
        return elementIndex.containsKey(o);
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
        boolean contained = elementIndex.containsKey(element);
        if (contained) {
            return false;
        }
        addNew(element);
        return true;
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        for (Object o : collection) {
            if (!elementIndex.containsKey(o)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends ELEMENT> collection) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        elementIndex.clear();
        elements.clear();
        predecessors.clear();
    }

    @Override
    public Set<Entry<ELEMENT>> getEntrySet() {
        final Set<Entry<ELEMENT>> result = new HashSet<>();
        int idx = 0;
        for (ELEMENT element : elements) {
            result.add(new Entry<>(element, toElements(predecessors.get(idx++))));
        }
        return result;
    }

    @Override
    public String toString() {
//        StringBuilder sb = new StringBuilder();
//        sb.append("PO{");
//        sb.append(elements);
//        sb.append(", [");
//        int i = 0;
//        for (Set<Integer> iPredecessors : predecessors) {
//            for (Integer iPredecessor : iPredecessors) {
//                sb.append("(");
//                sb.append(getElement(iPredecessor));
//                sb.append(" < ");
//                sb.append(getElement(i));
//                sb.append("), ");
//            }
//            i++;
//        }
//        sb = new StringBuilder(sb.substring(0, sb.length() - 2));
//        sb.append("]");
//        sb.append("}");
//        return sb.toString();
        return toGraphVizString();
    }

    public String toGraphVizString() {
        StringBuilder buildGViz = new StringBuilder("digraph G {\n");

        for (int i = 0; i < predecessors.size(); i++) {
            Set<Integer> iPredecessors = predecessors.get(i);
            for (Integer predecessor : iPredecessors) {
                buildGViz.append("\"" + getElement(predecessor) + "\" -> \"" + getElement(i) + "\";\n");
            }
            if (iPredecessors.isEmpty() && !hasSuccessors(i)) {
                buildGViz.append("\"" + getElement(i) + "\";\n");
            }
        }

        buildGViz.append("}");
        return buildGViz.toString();
    }

    private boolean hasSuccessors(int idx) {
        for (Set<Integer> predecessor : predecessors) {
            if (predecessor.contains(idx)) {
                return true;
            }
        }
        return false;
    }
}
