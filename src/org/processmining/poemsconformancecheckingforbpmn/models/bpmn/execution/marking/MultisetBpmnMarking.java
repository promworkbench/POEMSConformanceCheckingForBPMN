package org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.marking;

import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import org.processmining.models.graphbased.directed.bpmn.BPMNDiagram;
import org.processmining.models.graphbased.directed.bpmn.BPMNNode;
import org.processmining.models.graphbased.directed.bpmn.elements.Event;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.marking.token.BpmnToken;
import org.processmining.poemsconformancecheckingforbpmn.models.utils.multiset.Multiset;

import java.util.*;
import java.util.stream.Collectors;

public class MultisetBpmnMarking implements BpmnMarking {
    private final Multiset<BpmnToken> tokens;
    private final TObjectIntMap<BPMNNode> nodeProduced;
    private final TObjectIntMap<BPMNNode> nodeConsumed;
    private final BPMNDiagram model;
    private final transient int hash;

    public MultisetBpmnMarking(final BPMNDiagram model, final Multiset<BpmnToken> tokens) {
        this.tokens = tokens;
        this.model = model;
        this.nodeProduced = new TObjectIntHashMap<>(tokens.size());
        this.nodeConsumed = new TObjectIntHashMap<>();
        for (BpmnToken token : tokens) {
            this.nodeProduced.adjustOrPutValue(token.getSourceNode(), 1, 1);
            this.nodeConsumed.adjustOrPutValue(token.getSinkNode(), 1, 1);
        }
        hash = this.tokens.hashCode();
    }

    @Override
    public BPMNDiagram getModel() {
        return model;
    }

    public Set<BPMNNode> getReachableNodes() {
        return tokens.elementSet().stream().map(BpmnToken::getSinkNode).collect(Collectors.toSet());
    }

    @Override
    public int size() {
        return tokens.size();
    }

    @Override
    public boolean isEmpty() {
        return tokens.isEmpty();
    }

    @Override
    public boolean contains(Multiset<BpmnToken> mSet) {
        return tokens.contains(mSet);
    }

    @Override
    public boolean isContainedIn(Multiset<BpmnToken> mSet) {
        return tokens.isContainedIn(mSet);
    }

    @Override
    public boolean containsAtLeast(BpmnToken element, int count) {
        return tokens.containsAtLeast(element, count);
    }

    @Override
    public int count(Object element) {
        return tokens.count(element);
    }

    @Override
    public Set<BpmnToken> elementSet() {
        return tokens.elementSet();
    }

    @Override
    public Set<Entry<BpmnToken>> entrySet() {
        return tokens.entrySet();
    }

    @Override
    public boolean contains(Object obj) {
        return tokens.contains(obj);
    }

    @Override
    public boolean containsAll(Collection<?> elements) {
        return tokens.containsAll(elements);
    }

    @Override
    public boolean addAll(Collection<? extends BpmnToken> collection) {
        return false;
    }

    @Override
    public Iterator<BpmnToken> iterator() {
        return tokens.iterator();
    }

    @Override
    public Spliterator<BpmnToken> spliterator() {
        return tokens.spliterator();
    }

    @Override
    public Object[] toArray() {
        return tokens.toArray();
    }

    @Override
    public <T> T[] toArray(T[] var1) {
        return tokens.toArray(var1);
    }

    @Override
    public List<BpmnToken> asList() {
        return tokens.asList();
    }

    @Override
    public boolean isInitial() {
        if (tokens.isEmpty()) {
            return false;
        }
        for (BpmnToken token : tokens) {
            BPMNNode sourceNode = token.getSourceNode();
            if (!(sourceNode instanceof Event && Event.EventType.START.equals(((Event) sourceNode).getEventType()))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isFinal() {
        return isEmpty();
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof MultisetBpmnMarking)) return false;
        MultisetBpmnMarking that = (MultisetBpmnMarking) object;
        return Objects.equals(tokens, that.tokens);
    }

    @Override
    public int hashCode() {
        return hash;
    }

    @Override
    public int add(BpmnToken element, int count) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int remove(Object o, int count) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int setCount(BpmnToken element, int count) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean setCount(BpmnToken element, int count, int var3) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean add(BpmnToken element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> elements) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> elements) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return tokens.toString();
    }

    @Override
    public String toStringNewLines() {
        return tokens.toStringNewLines();
    }

    @Override
    public int nodeProducedTokensCount(BPMNNode sourceNode) {
        return nodeProduced.get(sourceNode);
    }

    @Override
    public int nodeConsumedTokensCount(BPMNNode sinkNode) {
        return nodeConsumed.get(sinkNode);
    }
}
