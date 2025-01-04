package org.processmining.poemsconformancecheckingforbpmn.models.trace.total_order.converters;

import org.processmining.earthmoversstochasticconformancechecking.stochasticlanguage.Activity2IndexKey;
import org.processmining.poemsconformancecheckingforbpmn.models.utils.activity.Activity;
import org.processmining.poemsconformancecheckingforbpmn.models.trace.total_order.TotallyOrderedTrace;

public interface TotallyOrderedTraceConverter<A extends Activity> {
    static <A extends Activity> TotallyOrderedTraceConverter<A> getInstance() {
        return new TotallyOrderedTraceConverterImpl<>();
    }

    int[] toEMSC24(TotallyOrderedTrace<A> trace, Activity2IndexKey activity2IndexKey);
}
