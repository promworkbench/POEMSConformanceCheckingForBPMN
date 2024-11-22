package org.processmining.sccwbpmnnpos.models.log;

import org.processmining.sccwbpmnnpos.models.utils.trace.total_order.TotallyOrderedTrace;

public interface SimplifiedEventLogTrace extends TotallyOrderedTrace {
    SimplifiedEventLog getEventLog();
}
