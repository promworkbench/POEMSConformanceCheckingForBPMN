package org.processmining.poemsconformancecheckingforbpmn.models.trace.partial_order;

import org.processmining.poemsconformancecheckingforbpmn.models.utils.activity.Activity;
import org.processmining.poemsconformancecheckingforbpmn.models.utils.ordered_set.partial.eventbased.EventBasedPartiallyOrderedSet;
import org.processmining.poemsconformancecheckingforbpmn.models.trace.Trace;

public interface PartiallyOrderedTrace<A extends Activity> extends EventBasedPartiallyOrderedSet<A>, Trace<A> {
}
