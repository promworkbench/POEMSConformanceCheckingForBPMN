package org.processmining.sccwbpmnnpos.models.log;

import org.processmining.sccwbpmnnpos.models.utils.activity.Activity;
import org.processmining.sccwbpmnnpos.models.trace.total_order.TotallyOrderedTrace;

public interface EventLogTrace extends TotallyOrderedTrace<Activity> {
    SimplifiedEventLog getEventLog();
}
