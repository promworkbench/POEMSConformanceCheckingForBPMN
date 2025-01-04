package org.processmining.poemsconformancecheckingforbpmn.models.trace.partial_order.converters;

import org.processmining.earthmoversstochasticconformancechecking.stochasticlanguage.Activity2IndexKey;
import org.processmining.poemsconformancecheckingforbpmn.models.utils.activity.Activity;
import org.processmining.poemsconformancecheckingforbpmn.models.trace.partial_order.PartiallyOrderedTrace;
import org.processmining.poemsconformancecheckingforbpmn.models.trace.partial_order.converters.emsc24.PartiallyOrderedTrace2EMSC24PartialOrderConverter;

public class PartiallyOrderedTraceConverterImpl<A extends Activity> implements PartiallyOrderedTraceConverter<A> {
    private final PartiallyOrderedTrace2EMSC24PartialOrderConverter<A> emsc24PartialOrderConverter;

    public PartiallyOrderedTraceConverterImpl(PartiallyOrderedTrace2EMSC24PartialOrderConverter<A> emsc24PartialOrderConverter) {
        this.emsc24PartialOrderConverter = emsc24PartialOrderConverter;
    }

    @Override
    public int[] toEMSC24(PartiallyOrderedTrace<A> poTrace, Activity2IndexKey activity2IndexKey) {
        return emsc24PartialOrderConverter.convert(poTrace, activity2IndexKey);
    }
}
