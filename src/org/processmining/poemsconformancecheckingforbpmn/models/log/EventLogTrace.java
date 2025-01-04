package org.processmining.poemsconformancecheckingforbpmn.models.log;

import org.processmining.poemsconformancecheckingforbpmn.models.utils.activity.Activity;
import org.processmining.poemsconformancecheckingforbpmn.models.trace.total_order.TotallyOrderedTrace;

public interface EventLogTrace extends TotallyOrderedTrace<Activity> {
    SimplifiedEventLog getEventLog();
}
