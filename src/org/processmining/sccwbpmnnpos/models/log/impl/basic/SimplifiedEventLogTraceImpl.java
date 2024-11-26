package org.processmining.sccwbpmnnpos.models.log.impl.basic;

import org.processmining.sccwbpmnnpos.models.log.SimplifiedEventLog;
import org.processmining.sccwbpmnnpos.models.log.SimplifiedEventLogTrace;
import org.processmining.sccwbpmnnpos.models.utils.activity.Activity;
import org.processmining.sccwbpmnnpos.models.utils.trace.total_order.TotallyOrderedTraceImpl;

public class SimplifiedEventLogTraceImpl extends TotallyOrderedTraceImpl<Activity> implements SimplifiedEventLogTrace {
    private final SimplifiedEventLog eventLog;

    public SimplifiedEventLogTraceImpl(SimplifiedEventLog eventLog) {
        super();
        this.eventLog = eventLog;
    }

    @Override
    public SimplifiedEventLog getEventLog() {
        return eventLog;
    }
}
