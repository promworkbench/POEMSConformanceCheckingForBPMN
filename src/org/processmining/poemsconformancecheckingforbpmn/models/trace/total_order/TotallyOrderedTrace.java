package org.processmining.poemsconformancecheckingforbpmn.models.trace.total_order;

import org.processmining.poemsconformancecheckingforbpmn.models.utils.activity.Activity;
import org.processmining.poemsconformancecheckingforbpmn.models.utils.ordered_set.total.TotallyOrderedSet;
import org.processmining.poemsconformancecheckingforbpmn.models.trace.Trace;

public interface TotallyOrderedTrace<A extends Activity> extends TotallyOrderedSet<A>, Trace<A> {
}
