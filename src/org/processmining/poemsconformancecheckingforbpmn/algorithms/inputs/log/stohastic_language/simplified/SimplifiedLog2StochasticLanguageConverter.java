package org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.log.stohastic_language.simplified;

import org.processmining.poemsconformancecheckingforbpmn.algorithms.utils.converters.Converter;
import org.processmining.poemsconformancecheckingforbpmn.models.log.stochastic.language.EventLogStochasticTOTraceLanguage;
import org.processmining.poemsconformancecheckingforbpmn.models.log.SimplifiedEventLog;

public interface SimplifiedLog2StochasticLanguageConverter extends Converter<SimplifiedEventLog, EventLogStochasticTOTraceLanguage> {
    static SimplifiedLog2StochasticLanguageConverter getInstance() {
        return new SimplifiedLog2StochasticLanguageConverterImpl();
    }

    EventLogStochasticTOTraceLanguage convert(final SimplifiedEventLog eventLog);
}
