package org.processmining.poemsconformancecheckingforbpmn.models.stochastic.language.trace.total.converter;

import org.processmining.earthmoversstochasticconformancechecking.stochasticlanguage.Activity2IndexKey;
import org.processmining.earthmoversstochasticconformancechecking.stochasticlanguage.StochasticLanguage;
import org.processmining.earthmoversstochasticconformancechecking.stochasticlanguage.TotalOrder;
import org.processmining.poemsconformancecheckingforbpmn.models.stochastic.language.trace.total.StochasticTOTraceLanguage;
import org.processmining.poemsconformancecheckingforbpmn.models.trace.total_order.TotallyOrderedTrace;
import org.processmining.poemsconformancecheckingforbpmn.models.trace.total_order.converters.TotallyOrderedTraceConverter;
import org.processmining.poemsconformancecheckingforbpmn.models.utils.activity.Activity;

public interface StochasticTraceTOLanguageConverter<A extends Activity, T extends TotallyOrderedTrace<A>> {
    static <A extends Activity, T extends TotallyOrderedTrace<A>> StochasticTraceTOLanguageConverter<A, T> getInstance() {
        return new StochasticTraceTOLanguageConverterImpl<>(TotallyOrderedTraceConverter.getInstance());
    }

    StochasticLanguage<TotalOrder> toEMSC24(StochasticTOTraceLanguage<A, T> traceLanguage,
                                            Activity2IndexKey activity2IndexKey);
}
