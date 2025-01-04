package org.processmining.poemsconformancecheckingforbpmn.models.trace.partial_order.converters;

import org.processmining.earthmoversstochasticconformancechecking.stochasticlanguage.Activity2IndexKey;
import org.processmining.poemsconformancecheckingforbpmn.models.utils.activity.Activity;
import org.processmining.poemsconformancecheckingforbpmn.models.trace.partial_order.PartiallyOrderedTrace;
import org.processmining.poemsconformancecheckingforbpmn.models.trace.partial_order.converters.emsc24.PartiallyOrderedTrace2EMSC24PartialOrderConverter;

public interface PartiallyOrderedTraceConverter<A extends Activity> {
    static <A extends Activity> PartiallyOrderedTraceConverter<A> getInstance() {
        return new PartiallyOrderedTraceConverterImpl<>(PartiallyOrderedTrace2EMSC24PartialOrderConverter.getInstance());
    }

    int[] toEMSC24(PartiallyOrderedTrace<A> poTrace, Activity2IndexKey activity2IndexKey);
}
