package org.processmining.poemsconformancecheckingforbpmn.models.trace.total_order;

import org.processmining.poemsconformancecheckingforbpmn.models.utils.activity.Activity;
import org.processmining.poemsconformancecheckingforbpmn.models.utils.ordered_set.total.ArrayTotallyOrderedSet;

public class TotallyOrderedTraceImpl<A extends Activity> extends ArrayTotallyOrderedSet<A> implements TotallyOrderedTrace<A> {

    public TotallyOrderedTraceImpl() {
    }
}
