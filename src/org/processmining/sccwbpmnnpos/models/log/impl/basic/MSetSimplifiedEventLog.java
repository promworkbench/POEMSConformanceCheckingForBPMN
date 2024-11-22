package org.processmining.sccwbpmnnpos.models.log.impl.basic;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import org.processmining.sccwbpmnnpos.models.log.SimplifiedEventLog;
import org.processmining.sccwbpmnnpos.models.log.SimplifiedEventLogTrace;
import org.processmining.sccwbpmnnpos.models.log.SimplifiedEventLogVariant;

import java.util.*;
import java.util.stream.Collectors;

public class MSetSimplifiedEventLog implements SimplifiedEventLog {
    private final Multiset<SimplifiedEventLogTrace> traces;


    public MSetSimplifiedEventLog() {
        this.traces = HashMultiset.create();
    }

    public void add(final SimplifiedEventLogTrace trace, final Integer cardinality) {
        assert Objects.nonNull(trace);
        assert Objects.nonNull(cardinality);
        assert cardinality > 0;
        traces.add(trace, cardinality);
    }

    public void remove(final SimplifiedEventLogTrace trace, final Integer cardinality) {
        assert Objects.nonNull(trace);
        assert Objects.nonNull(cardinality);
        assert cardinality > 0;
        traces.remove(trace, cardinality);
    }

    @Override
    public Set<String> getActivities() {
        return Collections.emptySet();
    }

    @Override
    public Collection<SimplifiedEventLogVariant> getVariants() {
        return traces.entrySet().stream().map(entry -> new BasicSimplifiedEventLogVariant(entry.getElement(),
                entry.getCount())).sorted(Comparator.comparing(BasicSimplifiedEventLogVariant::getCardinality).reversed()).collect(Collectors.toList());
    }

    @Override
    public int size() {
        return traces.size();
    }

    @Override
    public Collection<SimplifiedEventLogTrace> getTraces() {
        return traces;
    }

    @Override
    public Integer getTotalTraces() {
        return traces.size();
    }

    @Override
    public Iterator<SimplifiedEventLogVariant> iterator() {
        return getVariants().iterator();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        for (SimplifiedEventLogVariant variant : getVariants()) {
            sb.append(variant.toString());
            sb.append("\n");
        }
        return sb.toString();
    }
}
