package org.processmining.sccwbpmnnpos.algorithms.inputs.log.stohastic_language.simplified;

import org.processmining.sccwbpmnnpos.algorithms.utils.converters.Converter;
import org.processmining.sccwbpmnnpos.models.log.stochastic.language.EventLogStochasticTOTraceLanguage;
import org.processmining.sccwbpmnnpos.models.log.SimplifiedEventLog;

public interface SimplifiedLog2StochasticLanguageConverter extends Converter<SimplifiedEventLog, EventLogStochasticTOTraceLanguage> {
    static SimplifiedLog2StochasticLanguageConverter getInstance() {
        return new SimplifiedLog2StochasticLanguageConverterImpl();
    }

    EventLogStochasticTOTraceLanguage convert(final SimplifiedEventLog eventLog);
}
