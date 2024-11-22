package org.processmining.sccwbpmnnpos.models.utils.trace.partial_order;

import org.processmining.sccwbpmnnpos.models.utils.activity.Activity;
import org.processmining.sccwbpmnnpos.models.utils.ordered_set.partial.PartiallyOrderedSet;
import org.processmining.sccwbpmnnpos.models.utils.trace.Trace;

public interface PartiallyOrderedTrace extends PartiallyOrderedSet<Activity>, Trace {
}
