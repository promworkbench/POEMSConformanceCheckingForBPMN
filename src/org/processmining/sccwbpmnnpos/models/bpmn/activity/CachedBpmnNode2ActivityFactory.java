package org.processmining.sccwbpmnnpos.models.bpmn.activity;

import org.processmining.sccwbpmnnpos.models.utils.activity.Activity;
import org.processmining.sccwbpmnnpos.models.utils.activity.factory.ActivityFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CachedBpmnNode2ActivityFactory implements BpmnNode2ActivityFactory {
    private final Map<org.processmining.models.graphbased.directed.bpmn.elements.Activity, Activity> activityMap;
    private final ActivityFactory activityFactory;

    public CachedBpmnNode2ActivityFactory(ActivityFactory activityFactory) {
        activityMap = new HashMap<>();
        this.activityFactory = activityFactory;
    }

    @Override
    public Activity create(org.processmining.models.graphbased.directed.bpmn.elements.Activity bpmnElement) {
        Activity activity = activityMap.get(bpmnElement);
        if (Objects.isNull(activity)) {
            activity = activityFactory.create(bpmnElement.getLabel());
            activityMap.put(bpmnElement, activity);
        }
        return activity;
    }
}
