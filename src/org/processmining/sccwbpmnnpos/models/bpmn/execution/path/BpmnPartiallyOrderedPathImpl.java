package org.processmining.sccwbpmnnpos.models.bpmn.execution.path;

import org.processmining.models.graphbased.directed.bpmn.BPMNNode;
import org.processmining.plugins.graphviz.dot.Dot;
import org.processmining.sccwbpmnnpos.models.utils.ordered_set.exceptions.PartialOrderLoopNotAllowedException;
import org.processmining.sccwbpmnnpos.models.utils.ordered_set.partial.PartiallyOrderedSet;
import org.processmining.sccwbpmnnpos.models.utils.ordered_set.partial.eventbased.EventBasedPartiallyOrderedSet;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

public class BpmnPartiallyOrderedPathImpl implements BpmnPartiallyOrderedPath {
    private final EventBasedPartiallyOrderedSet<BPMNNode> path;

    public BpmnPartiallyOrderedPathImpl(EventBasedPartiallyOrderedSet<BPMNNode> path) {
        this.path = path;
    }

    @Override
    public Set<BPMNNode> getAlphabet() {
        return path.getAlphabet();
    }

    @Override
    public Dot toGraphViz() {
        return path.toGraphViz();
    }

    @Override
    public Event<BPMNNode> fire(BPMNNode item) {
        return path.fire(item);
    }

    @Override
    public Event<BPMNNode> fire(BPMNNode item, int executionIndex) {
        return path.fire(item, executionIndex);
    }

    @Override
    public Event<BPMNNode> fire(Event<BPMNNode> event) {
        return path.fire(event);
    }

    @Override
    public int getTimesFired(BPMNNode item) {
        return path.getTimesFired(item);
    }

    @Override
    public Event<BPMNNode> getEvent(BPMNNode item) {
        return path.getEvent(item);
    }

    @Override
    public Event<BPMNNode> getFiringEvent(BPMNNode item, int executionIndex) {
        return path.getFiringEvent(item, executionIndex);
    }

    @Override
    public PartiallyOrderedSet<Event<BPMNNode>> getPartiallyOrderedSet() {
        return path.getPartiallyOrderedSet();
    }

    @Override
    public void connect(Event<BPMNNode> predecessor, Event<BPMNNode> successor) throws PartialOrderLoopNotAllowedException {
        path.connect(predecessor, successor);
    }

    @Override
    public void connect(BPMNNode predecessor, int predecessorIdx, BPMNNode successor, int successorIdx) throws PartialOrderLoopNotAllowedException {
        path.connect(predecessor, predecessorIdx, successor, successorIdx);
    }

    @Override
    public void connectLast(BPMNNode predecessor, BPMNNode successor) throws PartialOrderLoopNotAllowedException {
        path.connectLast(predecessor, successor);
    }

    @Override
    public void concatenate(EventBasedPartiallyOrderedSet<BPMNNode> other) throws PartialOrderLoopNotAllowedException {
        path.concatenate(other);
    }

    @Override
    public int getNumberOfConnections() {
        return path.getNumberOfConnections();
    }

    @Override
    public int size() {
        return path.size();
    }

    @Override
    public boolean isEmpty() {
        return path.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return path.contains(o);
    }

    @Override
    public Iterator<BPMNNode> iterator() {
        return path.iterator();
    }

    @Override
    public Object[] toArray() {
        return path.toArray();
    }

    @Override
    public <T> T[] toArray(T[] ts) {
        return path.toArray(ts);
    }

    @Override
    public boolean add(BPMNNode node) {
        return path.add(node);
    }

    @Override
    public boolean remove(Object o) {
        return path.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return path.containsAll(collection);
    }

    @Override
    public boolean addAll(Collection<? extends BPMNNode> collection) {
        return path.addAll(collection);
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        return path.removeAll(collection);
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        return path.retainAll(collection);
    }

    @Override
    public void clear() {
        path.clear();
    }

    @Override
    public String toString() {
        return path.toString();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof BpmnPartiallyOrderedPathImpl)) return false;
        BpmnPartiallyOrderedPathImpl bpmnNodes = (BpmnPartiallyOrderedPathImpl) object;
        return Objects.equals(path, bpmnNodes.path);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(path);
    }
}
