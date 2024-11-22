package org.processmining.sccwbpmnnpos.models.utils.trace.total_order;

import org.processmining.sccwbpmnnpos.models.utils.activity.Activity;
import org.processmining.sccwbpmnnpos.models.utils.ordered_set.total.TotallyOrderedSet;
import org.processmining.sccwbpmnnpos.models.utils.trace.Trace;

public interface TotallyOrderedTrace extends TotallyOrderedSet<Activity>, Trace {
}
