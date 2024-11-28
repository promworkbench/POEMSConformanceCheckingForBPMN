package org.processmining.sccwbpmnnpos.models.log;

import org.processmining.sccwbpmnnpos.models.utils.activity.Activity;

public interface SimplifiedEventLogVariant extends Iterable<Activity> {
    SimplifiedEventLog getEventLog();

    EventLogTrace getTrace();

    Integer getCardinality();
}
