package org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.log.stohastic_language.simplified;

import org.processmining.poemsconformancecheckingforbpmn.models.log.SimplifiedEventLog;
import org.processmining.poemsconformancecheckingforbpmn.models.log.SimplifiedEventLogVariant;
import org.processmining.poemsconformancecheckingforbpmn.models.log.stochastic.language.EventLogStochasticTOTraceLanguage;
import org.processmining.poemsconformancecheckingforbpmn.models.log.stochastic.language.LinkedListEventLogStochasticTOTraceLanguage;
import org.processmining.stochasticbpmn.models.stochastic.Probability;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class SimplifiedLog2StochasticLanguageConverterImpl implements SimplifiedLog2StochasticLanguageConverter {
    @Override
    public EventLogStochasticTOTraceLanguage convert(SimplifiedEventLog eventLog) {
        final int logSize = eventLog.size();
        final LinkedListEventLogStochasticTOTraceLanguage stochasticLanguage =
                new LinkedListEventLogStochasticTOTraceLanguage();

        for (SimplifiedEventLogVariant variant : eventLog) {
            stochasticLanguage.add(variant.getTrace(),
                    Probability.of((BigDecimal.valueOf(variant.getCardinality()).divide(BigDecimal.valueOf(logSize),
                            30, RoundingMode.DOWN)).stripTrailingZeros()));
        }
        return stochasticLanguage;
    }
}
