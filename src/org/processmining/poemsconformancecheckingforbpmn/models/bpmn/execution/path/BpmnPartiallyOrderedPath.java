package org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.path;

import org.processmining.models.graphbased.directed.bpmn.BPMNNode;
import org.processmining.poemsconformancecheckingforbpmn.models.utils.ordered_set.partial.eventbased.EventBasedPartiallyOrderedSet;
import org.processmining.poemsconformancecheckingforbpmn.models.utils.ordered_set.partial.eventbased.NonRepetitiveEventBasedPartiallyOrderedSet;
import org.processmining.poemsconformancecheckingforbpmn.models.utils.ordered_set.partial.eventbased.RepetitiveEventBasedPartiallyOrderedSet;

public interface BpmnPartiallyOrderedPath extends EventBasedPartiallyOrderedSet<BPMNNode> {
    static BpmnPartiallyOrderedPath getRepetitiveInstance() {
        return new BpmnPartiallyOrderedPathImpl(new RepetitiveEventBasedPartiallyOrderedSet<>());
    }

    static BpmnPartiallyOrderedPath getNonRepetitiveInstance() {
        return new BpmnPartiallyOrderedPathImpl(new NonRepetitiveEventBasedPartiallyOrderedSet<>());
    }
}
