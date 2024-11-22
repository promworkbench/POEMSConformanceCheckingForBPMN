package org.processmining.sccwbpmnnpos.models.utils.trace;

import org.processmining.sccwbpmnnpos.models.utils.activity.Activity;
import org.processmining.sccwbpmnnpos.models.utils.ordered_set.OrderedSet;

public interface Trace extends OrderedSet<Activity> {
    String getLabel();

    Integer getId();
}
