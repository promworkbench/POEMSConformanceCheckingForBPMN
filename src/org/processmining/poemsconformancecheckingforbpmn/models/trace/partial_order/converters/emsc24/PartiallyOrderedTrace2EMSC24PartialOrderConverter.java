package org.processmining.poemsconformancecheckingforbpmn.models.trace.partial_order.converters.emsc24;

import org.processmining.earthmoversstochasticconformancechecking.stochasticlanguage.Activity2IndexKey;
import org.processmining.poemsconformancecheckingforbpmn.models.utils.activity.Activity;
import org.processmining.poemsconformancecheckingforbpmn.models.trace.partial_order.PartiallyOrderedTrace;

public interface PartiallyOrderedTrace2EMSC24PartialOrderConverter<T extends Activity> {
    static <T extends Activity> PartiallyOrderedTrace2EMSC24PartialOrderConverter<T> getInstance() {
        return new PartiallyOrderedTrace2EMSC24PartialOrderConverterImpl<T>();
    }

    int[] convert(final PartiallyOrderedTrace<T> poTrace, Activity2IndexKey activity2IndexKey);
}
