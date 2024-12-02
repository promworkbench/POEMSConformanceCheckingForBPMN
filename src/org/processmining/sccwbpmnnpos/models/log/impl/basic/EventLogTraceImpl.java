package org.processmining.sccwbpmnnpos.models.log.impl.basic;

import org.processmining.sccwbpmnnpos.models.log.SimplifiedEventLog;
import org.processmining.sccwbpmnnpos.models.log.EventLogTrace;
import org.processmining.sccwbpmnnpos.models.utils.activity.Activity;
import org.processmining.sccwbpmnnpos.models.trace.total_order.TotallyOrderedTraceImpl;

public class EventLogTraceImpl extends TotallyOrderedTraceImpl<Activity> implements EventLogTrace {
    private final SimplifiedEventLog eventLog;

    public EventLogTraceImpl(SimplifiedEventLog eventLog) {
        super();
        this.eventLog = eventLog;
    }

    @Override
    public SimplifiedEventLog getEventLog() {
        return eventLog;
    }
}