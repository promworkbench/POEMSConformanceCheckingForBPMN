package org.processmining.poemsconformancecheckingforbpmn.models.log;

import java.util.Collection;
import java.util.Set;


public interface SimplifiedEventLog extends Iterable<SimplifiedEventLogVariant> {
    Set<String> getActivities();

    Collection<SimplifiedEventLogVariant> getVariants();

    int size();

    Collection<EventLogTrace> getTraces();

    Integer getTotalTraces();
}
