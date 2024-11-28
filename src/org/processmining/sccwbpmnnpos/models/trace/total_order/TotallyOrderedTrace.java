package org.processmining.sccwbpmnnpos.models.trace.total_order;

import org.processmining.sccwbpmnnpos.models.utils.activity.Activity;
import org.processmining.sccwbpmnnpos.models.utils.ordered_set.total.TotallyOrderedSet;
import org.processmining.sccwbpmnnpos.models.trace.Trace;

public interface TotallyOrderedTrace<A extends Activity> extends TotallyOrderedSet<A>, Trace<A> {
}
