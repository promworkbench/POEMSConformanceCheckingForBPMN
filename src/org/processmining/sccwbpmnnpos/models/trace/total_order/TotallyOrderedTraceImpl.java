package org.processmining.sccwbpmnnpos.models.trace.total_order;

import org.processmining.sccwbpmnnpos.models.utils.activity.Activity;
import org.processmining.sccwbpmnnpos.models.utils.ordered_set.total.ArrayTotallyOrderedSet;

public class TotallyOrderedTraceImpl<A extends Activity> extends ArrayTotallyOrderedSet<A> implements TotallyOrderedTrace<A> {

    public TotallyOrderedTraceImpl() {
    }
}
