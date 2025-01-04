package org.processmining.poemsconformancecheckingforbpmn.models.log.impl.basic;

import org.processmining.poemsconformancecheckingforbpmn.models.log.SimplifiedEventLog;
import org.processmining.poemsconformancecheckingforbpmn.models.log.EventLogTrace;
import org.processmining.poemsconformancecheckingforbpmn.models.utils.activity.Activity;
import org.processmining.poemsconformancecheckingforbpmn.models.trace.total_order.TotallyOrderedTraceImpl;

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
