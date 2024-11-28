package org.processmining.sccwbpmnnpos.algorithms.inputs.log.stohastic_language.xlog;

import org.deckfour.xes.model.XLog;
import org.processmining.sccwbpmnnpos.algorithms.inputs.log.simplifier.XLogSimplifier;
import org.processmining.sccwbpmnnpos.algorithms.inputs.log.stohastic_language.simplified.SimplifiedLog2StochasticLanguageConverter;
import org.processmining.sccwbpmnnpos.models.log.SimplifiedEventLog;
import org.processmining.sccwbpmnnpos.models.log.stochastic.language.EventLogStochasticTOTraceLanguage;

public class XLog2StochasticLanguageConverterImpl implements Xlog2StochasticTraceLanguageConverter {
    final XLogSimplifier logSimplifier;
    final SimplifiedLog2StochasticLanguageConverter log2StochasticLanguage;

    public XLog2StochasticLanguageConverterImpl(XLogSimplifier logSimplifier,
                                                SimplifiedLog2StochasticLanguageConverter log2StochasticLanguage) {
        this.logSimplifier = logSimplifier;
        this.log2StochasticLanguage = log2StochasticLanguage;
    }

    @Override
    public EventLogStochasticTOTraceLanguage convert(XLog log) {
        SimplifiedEventLog simplifiedEventLog = logSimplifier.simplify(log);
        return log2StochasticLanguage.convert(simplifiedEventLog);
    }
}
