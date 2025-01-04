package org.processmining.poemsconformancecheckingforbpmn.models.log.impl.basic;

import org.processmining.poemsconformancecheckingforbpmn.models.log.SimplifiedEventLog;
import org.processmining.poemsconformancecheckingforbpmn.models.log.EventLogTrace;
import org.processmining.poemsconformancecheckingforbpmn.models.log.SimplifiedEventLogVariant;
import org.processmining.poemsconformancecheckingforbpmn.models.utils.activity.Activity;

import java.util.Iterator;
import java.util.Objects;

public class BasicSimplifiedEventLogVariant implements SimplifiedEventLogVariant {
    private final EventLogTrace trace;
    private final Integer cardinality;

    public BasicSimplifiedEventLogVariant(EventLogTrace trace, Integer cardinality) {
        assert Objects.nonNull(trace);
        assert Objects.nonNull(cardinality);
        assert cardinality > 0;
        this.trace = trace;
        this.cardinality = cardinality;
    }


    @Override
    public SimplifiedEventLog getEventLog() {
        return trace.getEventLog();
    }

    @Override
    public EventLogTrace getTrace() {
        return trace;
    }

    @Override
    public Integer getCardinality() {
        return cardinality;
    }


    @Override
    public Iterator<Activity> iterator() {
        return this.trace.iterator();
    }


    @Override
    public String toString() {
        return cardinality + ": " + trace;
    }
}
