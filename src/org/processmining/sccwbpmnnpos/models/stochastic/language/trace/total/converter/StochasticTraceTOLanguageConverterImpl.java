package org.processmining.sccwbpmnnpos.models.stochastic.language.trace.total.converter;

import org.processmining.earthmoversstochasticconformancechecking.stochasticlanguage.Activity2IndexKey;
import org.processmining.earthmoversstochasticconformancechecking.stochasticlanguage.StochasticLanguage;
import org.processmining.earthmoversstochasticconformancechecking.stochasticlanguage.TotalOrder;
import org.processmining.sccwbpmnnpos.models.stochastic.language.StochasticLanguageEntry;
import org.processmining.sccwbpmnnpos.models.stochastic.language.trace.EMSC24StochasticLanguageAdapter;
import org.processmining.sccwbpmnnpos.models.stochastic.language.trace.total.StochasticTOTraceLanguage;
import org.processmining.sccwbpmnnpos.models.trace.total_order.TotallyOrderedTrace;
import org.processmining.sccwbpmnnpos.models.trace.total_order.converters.TotallyOrderedTraceConverter;
import org.processmining.sccwbpmnnpos.models.utils.activity.Activity;

import java.util.ArrayList;

public class StochasticTraceTOLanguageConverterImpl<A extends Activity, T extends TotallyOrderedTrace<A>> implements StochasticTraceTOLanguageConverter<A, T> {
    private final TotallyOrderedTraceConverter<A> traceConverter;

    public StochasticTraceTOLanguageConverterImpl(TotallyOrderedTraceConverter<A> traceConverter) {
        this.traceConverter = traceConverter;
    }

    @Override
    public StochasticLanguage<TotalOrder> toEMSC24(StochasticTOTraceLanguage<A, T> traceLanguage,
                                                   Activity2IndexKey activity2IndexKey) {
        ArrayList<int[]> traces = new ArrayList<>();
        double[] probabilities = new double[traceLanguage.getSize()];

        int i = 0;
        for (StochasticLanguageEntry<A, T> entry : traceLanguage) {
            traces.add(traceConverter.toEMSC24(entry.getElement(), activity2IndexKey));
            probabilities[i] = entry.getProbability().doubleValue();
        }
        return new EMSC24StochasticLanguageAdapter<>(activity2IndexKey, traces, probabilities);
    }
}
