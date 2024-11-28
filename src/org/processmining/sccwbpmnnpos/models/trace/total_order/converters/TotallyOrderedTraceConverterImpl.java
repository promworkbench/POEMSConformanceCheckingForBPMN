package org.processmining.sccwbpmnnpos.models.trace.total_order.converters;

import org.processmining.earthmoversstochasticconformancechecking.stochasticlanguage.Activity2IndexKey;
import org.processmining.sccwbpmnnpos.models.utils.activity.Activity;
import org.processmining.sccwbpmnnpos.models.trace.total_order.TotallyOrderedTrace;

public class TotallyOrderedTraceConverterImpl<A extends Activity> implements TotallyOrderedTraceConverter<A> {
    @Override
    public int[] toEMSC24(TotallyOrderedTrace<A> trace, Activity2IndexKey activity2IndexKey) {
        int[] newTrace = new int[trace.size()];
        int i = 0;
        for (A a : trace) {
            newTrace[i++] = activity2IndexKey.toIndex(a.getLabel());
        }
        return newTrace;
    }
}
