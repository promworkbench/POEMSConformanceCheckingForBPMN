package org.processmining.sccwbpmnnpos.models.utils.activity;

import java.util.Objects;

public class ActivityImpl implements Activity {
    private final String label;
    private final Integer id;
    private final ActivityRegistry registry;

    public ActivityImpl(String label, ActivityRegistry activityRegistry) {
        this.label = label;
        this.registry = activityRegistry;
        this.id = activityRegistry.register(this);
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public ActivityRegistry getRegistry() {
        return registry;
    }

    @Override
    public String toString() {
        return this.label;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ActivityImpl)) return false;
        ActivityImpl activity = (ActivityImpl) o;
        return Objects.equals(id, activity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
