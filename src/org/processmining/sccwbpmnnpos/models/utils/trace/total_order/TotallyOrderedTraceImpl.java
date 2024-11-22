package org.processmining.sccwbpmnnpos.models.utils.trace.total_order;

import org.processmining.sccwbpmnnpos.models.utils.activity.Activity;
import org.processmining.sccwbpmnnpos.models.utils.ordered_set.total.ArrayTotallyOrderedSet;

public class TotallyOrderedTraceImpl extends ArrayTotallyOrderedSet<Activity> implements TotallyOrderedTrace {
    private final Integer id;
    private final String label;

    public TotallyOrderedTraceImpl(Integer id, String label) {
        this.id = id;
        this.label = label;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public Integer getId() {
        return id;
    }
}
