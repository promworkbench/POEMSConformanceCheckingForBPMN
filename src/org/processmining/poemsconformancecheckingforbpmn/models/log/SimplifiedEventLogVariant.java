package org.processmining.poemsconformancecheckingforbpmn.models.log;

import org.processmining.poemsconformancecheckingforbpmn.models.utils.activity.Activity;

public interface SimplifiedEventLogVariant extends Iterable<Activity> {
    SimplifiedEventLog getEventLog();

    EventLogTrace getTrace();

    Integer getCardinality();
}
