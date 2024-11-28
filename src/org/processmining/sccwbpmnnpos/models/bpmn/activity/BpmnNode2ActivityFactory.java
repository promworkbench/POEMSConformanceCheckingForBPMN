package org.processmining.sccwbpmnnpos.models.bpmn.activity;

import org.processmining.sccwbpmnnpos.models.utils.activity.Activity;
import org.processmining.sccwbpmnnpos.models.utils.activity.factory.ActivityFactory;

public interface BpmnNode2ActivityFactory {
    static BpmnNode2ActivityFactory getInstance(ActivityFactory activityFactory) {
        return new CachedBpmnNode2ActivityFactory(activityFactory);
    }

    Activity create(org.processmining.models.graphbased.directed.bpmn.elements.Activity bpmnElement);
}
