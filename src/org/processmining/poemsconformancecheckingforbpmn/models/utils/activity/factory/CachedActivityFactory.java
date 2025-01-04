package org.processmining.poemsconformancecheckingforbpmn.models.utils.activity.factory;

import org.processmining.poemsconformancecheckingforbpmn.models.utils.activity.Activity;
import org.processmining.poemsconformancecheckingforbpmn.models.utils.activity.ActivityImpl;

import java.util.*;

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

    @Override
    public Collection<Activity> getAlphabet() {
        return activityMap.values();
    }


}
