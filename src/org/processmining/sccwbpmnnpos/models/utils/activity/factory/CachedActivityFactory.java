package org.processmining.sccwbpmnnpos.models.utils.activity.factory;

import org.processmining.sccwbpmnnpos.models.utils.activity.Activity;
import org.processmining.sccwbpmnnpos.models.utils.activity.ActivityImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CachedActivityFactory implements ActivityFactory {
    final Map<String, Activity> activityMap;

    public CachedActivityFactory() {
        this.activityMap = new HashMap<>();
    }

    @Override
    public synchronized Activity create(String label) {
        Activity activity = activityMap.get(label);
        if (Objects.isNull(activity)) {
            int nextIndex = activityMap.size();
            activity = new ActivityImpl(nextIndex, label);
            activityMap.put(label, activity);
        }
        return activity;
    }
}
