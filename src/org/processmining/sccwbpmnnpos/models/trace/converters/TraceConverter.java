package org.processmining.sccwbpmnnpos.models.trace.converters;

import org.processmining.earthmoversstochasticconformancechecking.stochasticlanguage.Activity2IndexKey;
import org.processmining.sccwbpmnnpos.models.utils.activity.Activity;
import org.processmining.sccwbpmnnpos.models.trace.partial_order.PartiallyOrderedTrace;
import org.processmining.sccwbpmnnpos.models.trace.partial_order.converters.PartiallyOrderedTraceConverter;
import org.processmining.sccwbpmnnpos.models.trace.total_order.TotallyOrderedTrace;
import org.processmining.sccwbpmnnpos.models.trace.total_order.converters.TotallyOrderedTraceConverter;

public interface TraceConverter<A extends Activity> {
    static <A extends Activity> TraceConverter<A> getInstance() {
        return new TraceConverterImpl<>(PartiallyOrderedTraceConverter.getInstance(),
                TotallyOrderedTraceConverter.getInstance());
    }

    int[] toEMSC24(final PartiallyOrderedTrace<A> poTrace, Activity2IndexKey activity2IndexKey);

    int[] toEMSC24(TotallyOrderedTrace<A> trace, Activity2IndexKey activity2IndexKey);
}
