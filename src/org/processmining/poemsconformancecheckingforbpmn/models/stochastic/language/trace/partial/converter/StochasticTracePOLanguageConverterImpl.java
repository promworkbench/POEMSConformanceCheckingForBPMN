package org.processmining.poemsconformancecheckingforbpmn.models.stochastic.language.trace.partial.converter;

import org.processmining.earthmoversstochasticconformancechecking.stochasticlanguage.Activity2IndexKey;
import org.processmining.earthmoversstochasticconformancechecking.stochasticlanguage.PartialOrderCertain;
import org.processmining.earthmoversstochasticconformancechecking.stochasticlanguage.StochasticLanguage;
import org.processmining.poemsconformancecheckingforbpmn.models.stochastic.language.StochasticLanguageEntry;
import org.processmining.poemsconformancecheckingforbpmn.models.stochastic.language.trace.EMSC24StochasticLanguageAdapter;
import org.processmining.poemsconformancecheckingforbpmn.models.stochastic.language.trace.partial.StochasticPOTraceLanguage;
import org.processmining.poemsconformancecheckingforbpmn.models.trace.partial_order.PartiallyOrderedTrace;
import org.processmining.poemsconformancecheckingforbpmn.models.trace.partial_order.converters.PartiallyOrderedTraceConverter;
import org.processmining.poemsconformancecheckingforbpmn.models.utils.activity.Activity;

import java.util.ArrayList;

public class StochasticTracePOLanguageConverterImpl<A extends Activity, T extends PartiallyOrderedTrace<A>> implements StochasticTracePOLanguageConverter<A, T> {
    private final PartiallyOrderedTraceConverter<A> traceConverter;

    public StochasticTracePOLanguageConverterImpl(PartiallyOrderedTraceConverter<A> traceConverter) {
        this.traceConverter = traceConverter;
    }

    @Override
    public StochasticLanguage<PartialOrderCertain> toEMSC24(StochasticPOTraceLanguage<A, T> traceLanguage,
                                                            Activity2IndexKey activity2IndexKey) {
        ArrayList<int[]> traces = new ArrayList<>();
        double[] probabilities = new double[traceLanguage.size()];

        int i = 0;
        for (StochasticLanguageEntry<A, T> entry : traceLanguage) {
            traces.add(traceConverter.toEMSC24(entry.getElement(), activity2IndexKey));
            probabilities[i++] = entry.getProbability().doubleValue();
        }
        return new EMSC24StochasticLanguageAdapter<>(activity2IndexKey, traces, probabilities);
    }
}
