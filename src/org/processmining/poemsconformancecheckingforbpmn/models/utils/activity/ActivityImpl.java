package org.processmining.poemsconformancecheckingforbpmn.models.utils.activity;

import java.util.Objects;

public class ActivityImpl implements Activity {
    private final String label;
    private final Integer id;

    public ActivityImpl(int id, String label) {
        this.label = label;
        this.id = id;
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

    @Override
    public String toString() {
        return getLabel();
    }
}
