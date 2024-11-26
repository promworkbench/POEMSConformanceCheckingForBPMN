package org.processmining.sccwbpmnnpos.algorithms.inputs.log.stohastic_language;

import org.processmining.sccwbpmnnpos.models.log.SimplifiedEventLogTrace;
import org.processmining.sccwbpmnnpos.models.log.SimplifiedEventLogVariant;
import org.processmining.sccwbpmnnpos.models.stochastic.language.LinkedListStochasticLanguage;
import org.processmining.sccwbpmnnpos.models.utils.activity.Activity;
import org.processmining.sccwbpmnnpos.models.utils.trace.total_order.TotallyOrderedTrace;
import org.processmining.sccwbpmnnpos.models.log.SimplifiedEventLog;
import org.processmining.sccwbpmnnpos.models.stochastic.language.StochasticLanguage;
import org.processmining.stochasticbpmn.models.stochastic.Probability;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class SimplifiedLogToStochasticLanguageConverterImpl implements SimplifiedLogToStochasticLanguageConverter {
    @Override
    public StochasticLanguage<Activity, SimplifiedEventLogTrace> convert(SimplifiedEventLog eventLog) {
        final int logSize = eventLog.size();
        final LinkedListStochasticLanguage<Activity, SimplifiedEventLogTrace> stochasticLanguage = new LinkedListStochasticLanguage<>();

        for (SimplifiedEventLogVariant variant : eventLog) {
            stochasticLanguage.add(variant.getTrace(), Probability.of(BigDecimal.valueOf(variant.getCardinality()).divide(BigDecimal.valueOf(logSize), 30, RoundingMode.DOWN).stripTrailingZeros()));
        }
        return stochasticLanguage;
    }
}
