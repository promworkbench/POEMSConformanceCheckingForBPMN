package org.processmining.sccwbpmnnpos.algorithms.inputs.log.stohastic_language.xlog;

import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.model.XLog;
import org.processmining.sccwbpmnnpos.algorithms.inputs.log.simplifier.XLogSimplifier;
import org.processmining.sccwbpmnnpos.algorithms.inputs.log.stohastic_language.simplified.SimplifiedLog2StochasticLanguageConverter;
import org.processmining.sccwbpmnnpos.algorithms.utils.converters.Converter;
import org.processmining.sccwbpmnnpos.models.log.stochastic.language.EventLogStochasticTOTraceLanguage;
import org.processmining.sccwbpmnnpos.models.utils.activity.factory.ActivityFactory;

public interface Xlog2StochasticTraceLanguageConverter extends Converter<XLog, EventLogStochasticTOTraceLanguage> {
    static Xlog2StochasticTraceLanguageConverter getInstance(XEventClassifier classifier,
                                                             ActivityFactory activityFactory) {
        return new XLog2StochasticLanguageConverterImpl(XLogSimplifier.getInstance(classifier, activityFactory),
                SimplifiedLog2StochasticLanguageConverter.getInstance());
    }
}
