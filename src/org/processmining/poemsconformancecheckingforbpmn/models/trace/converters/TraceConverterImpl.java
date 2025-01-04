package org.processmining.poemsconformancecheckingforbpmn.models.trace.converters;

import org.processmining.earthmoversstochasticconformancechecking.stochasticlanguage.Activity2IndexKey;
import org.processmining.poemsconformancecheckingforbpmn.models.utils.activity.Activity;
import org.processmining.poemsconformancecheckingforbpmn.models.trace.partial_order.PartiallyOrderedTrace;
import org.processmining.poemsconformancecheckingforbpmn.models.trace.partial_order.converters.PartiallyOrderedTraceConverter;
import org.processmining.poemsconformancecheckingforbpmn.models.trace.total_order.TotallyOrderedTrace;
import org.processmining.poemsconformancecheckingforbpmn.models.trace.total_order.converters.TotallyOrderedTraceConverter;

public class TraceConverterImpl<A extends Activity> implements TraceConverter<A> {
    private final PartiallyOrderedTraceConverter<A> poConverter;
    private final TotallyOrderedTraceConverter<A> toConverter;

    public TraceConverterImpl(PartiallyOrderedTraceConverter<A> poConverter,
                              TotallyOrderedTraceConverter<A> toConverter) {
        this.poConverter = poConverter;
        this.toConverter = toConverter;
    }

    @Override
    public int[] toEMSC24(PartiallyOrderedTrace<A> poTrace, Activity2IndexKey activity2IndexKey) {
        return poConverter.toEMSC24(poTrace, activity2IndexKey);
    }

    @Override
    public int[] toEMSC24(TotallyOrderedTrace<A> trace, Activity2IndexKey activity2IndexKey) {
        return toConverter.toEMSC24(trace, activity2IndexKey);
    }
}
