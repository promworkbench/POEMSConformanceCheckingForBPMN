package org.processmining.poemsconformancecheckingforbpmn.models.stochastic.language.trace.partial.converter;

import org.processmining.earthmoversstochasticconformancechecking.stochasticlanguage.Activity2IndexKey;
import org.processmining.earthmoversstochasticconformancechecking.stochasticlanguage.PartialOrderCertain;
import org.processmining.earthmoversstochasticconformancechecking.stochasticlanguage.StochasticLanguage;
import org.processmining.poemsconformancecheckingforbpmn.models.stochastic.language.trace.partial.StochasticPOTraceLanguage;
import org.processmining.poemsconformancecheckingforbpmn.models.trace.partial_order.PartiallyOrderedTrace;
import org.processmining.poemsconformancecheckingforbpmn.models.trace.partial_order.converters.PartiallyOrderedTraceConverter;
import org.processmining.poemsconformancecheckingforbpmn.models.utils.activity.Activity;

public interface StochasticTracePOLanguageConverter<A extends Activity, T extends PartiallyOrderedTrace<A>> {
    static <A extends Activity, T extends PartiallyOrderedTrace<A>> StochasticTracePOLanguageConverter<A, T> getInstance() {
        return new StochasticTracePOLanguageConverterImpl<>(PartiallyOrderedTraceConverter.getInstance());
    }

    StochasticLanguage<PartialOrderCertain> toEMSC24(StochasticPOTraceLanguage<A, T> traceLanguage,
                                                     Activity2IndexKey activity2IndexKey);
}
