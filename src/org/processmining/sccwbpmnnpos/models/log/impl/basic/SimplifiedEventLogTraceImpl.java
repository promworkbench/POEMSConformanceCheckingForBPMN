package org.processmining.sccwbpmnnpos.models.log.impl.basic;

import org.processmining.sccwbpmnnpos.models.log.SimplifiedEventLog;
import org.processmining.sccwbpmnnpos.models.log.SimplifiedEventLogTrace;
import org.processmining.sccwbpmnnpos.models.utils.trace.total_order.TotallyOrderedTraceImpl;

public class SimplifiedEventLogTraceImpl extends TotallyOrderedTraceImpl implements SimplifiedEventLogTrace {
    private final SimplifiedEventLog eventLog;

    public SimplifiedEventLogTraceImpl(SimplifiedEventLog eventLog) {
        super(0, "");
        this.eventLog = eventLog;
    }

    @Override
    public SimplifiedEventLog getEventLog() {
        return eventLog;
    }
}
