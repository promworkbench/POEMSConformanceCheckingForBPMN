package org.processmining.sccwbpmnnpos.algorithms.inputs.log.stohastic_language;

import org.processmining.sccwbpmnnpos.models.log.SimplifiedEventLogVariant;
import org.processmining.sccwbpmnnpos.models.stochastic_language.MapStochasticLanguage;
import org.processmining.sccwbpmnnpos.models.utils.activity.Activity;
import org.processmining.sccwbpmnnpos.models.utils.trace.total_order.TotallyOrderedTrace;
import org.processmining.sccwbpmnnpos.models.log.SimplifiedEventLog;
import org.processmining.sccwbpmnnpos.models.stochastic_language.StochasticLanguage;

public class SimplifiedLogToStochasticLanguageConverterImpl implements SimplifiedLogToStochasticLanguageConverter {
    @Override
    public StochasticLanguage<Activity, TotallyOrderedTrace> convert(SimplifiedEventLog eventLog) {
        final int logSize = eventLog.size();
        final StochasticLanguage<Activity, TotallyOrderedTrace> stochasticLanguage = new MapStochasticLanguage<>();

        for (SimplifiedEventLogVariant variant : eventLog) {
            stochasticLanguage.put(variant.getTrace(), variant.getCardinality() * 1.0 / logSize);
        }
        return stochasticLanguage;
    }
}
