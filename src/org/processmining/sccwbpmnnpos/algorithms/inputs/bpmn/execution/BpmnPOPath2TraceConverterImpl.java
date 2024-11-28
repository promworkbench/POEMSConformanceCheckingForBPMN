package org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.execution;

import org.processmining.models.graphbased.directed.bpmn.BPMNNode;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.path.BpmnPartiallyOrderedPath;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.trace.BpmnPartiallyOrderedTrace;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.trace.BpmnPartiallyOrderedTraceImpl;
import org.processmining.sccwbpmnnpos.models.bpmn.activity.BpmnNode2ActivityFactory;
import org.processmining.sccwbpmnnpos.models.utils.activity.Activity;
import org.processmining.sccwbpmnnpos.models.utils.ordered_set.exceptions.PartialOrderLoopNotAllowedException;
import org.processmining.sccwbpmnnpos.models.utils.ordered_set.partial.eventbased.EventBasedPartiallyOrderedSet.Event;

import java.util.*;

public class BpmnPOPath2TraceConverterImpl implements BpmnPOPath2TraceConverter {
    private final BpmnNode2ActivityFactory activityFactory;

    public BpmnPOPath2TraceConverterImpl(BpmnNode2ActivityFactory activityFactory) {
        this.activityFactory = activityFactory;
    }

    @Override
    public BpmnPartiallyOrderedTrace convert(BpmnPartiallyOrderedPath path) {
        BpmnPartiallyOrderedTrace trace = new BpmnPartiallyOrderedTraceImpl();
        Map<Event<BPMNNode>, Event<Activity>> nodeToActivityEventMap = new HashMap<>();
        for (Event<BPMNNode> nodeEvent : path.getPartiallyOrderedSet()) {
            if (!(nodeEvent.getItem() instanceof org.processmining.models.graphbased.directed.bpmn.elements.Activity)) {
                continue;
            }
            Activity activity =
                    activityFactory.create((org.processmining.models.graphbased.directed.bpmn.elements.Activity) (nodeEvent.getItem()));
            Event<Activity> activityEvent = trace.fire(activity);
            nodeToActivityEventMap.put(nodeEvent, activityEvent);

            Set<Event<BPMNNode>> predecessors = path.getPartiallyOrderedSet().getPredecessors(nodeEvent);
            Queue<Event<BPMNNode>> queue = new LinkedList<>(predecessors);
            while (!queue.isEmpty()) {
                Event<BPMNNode> predecessorNodeEvent = queue.poll();
                Event<Activity> predecessorActivityEvent = nodeToActivityEventMap.get(predecessorNodeEvent);
                if (Objects.nonNull(predecessorActivityEvent)) {
                    try {
                        trace.connect(predecessorActivityEvent, activityEvent);
                    } catch (PartialOrderLoopNotAllowedException e) {
                        throw new RuntimeException("Loops should not be problem while converting BPMN path to model " +
                                "trace. Fix this.", e);
                    }
                } else {
                    queue.addAll(path.getPartiallyOrderedSet().getPredecessors(predecessorNodeEvent));
                }
            }
        }
        return trace;
    }
}
