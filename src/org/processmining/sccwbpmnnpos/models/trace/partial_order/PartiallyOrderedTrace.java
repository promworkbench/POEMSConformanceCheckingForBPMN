package org.processmining.sccwbpmnnpos.models.trace.partial_order;

import org.processmining.sccwbpmnnpos.models.utils.activity.Activity;
import org.processmining.sccwbpmnnpos.models.utils.ordered_set.partial.eventbased.EventBasedPartiallyOrderedSet;
import org.processmining.sccwbpmnnpos.models.trace.Trace;

public interface PartiallyOrderedTrace<A extends Activity> extends EventBasedPartiallyOrderedSet<A>, Trace<A> {
}
