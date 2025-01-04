package org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.log.stohastic_language.xlog;

import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.model.XLog;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.log.simplifier.XLogSimplifier;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.log.stohastic_language.simplified.SimplifiedLog2StochasticLanguageConverter;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.utils.converters.Converter;
import org.processmining.poemsconformancecheckingforbpmn.models.log.stochastic.language.EventLogStochasticTOTraceLanguage;
import org.processmining.poemsconformancecheckingforbpmn.models.utils.activity.factory.ActivityFactory;

public interface Xlog2StochasticTraceLanguageConverter extends Converter<XLog, EventLogStochasticTOTraceLanguage> {
    static Xlog2StochasticTraceLanguageConverter getInstance(XEventClassifier classifier,
                                                             ActivityFactory activityFactory) {
        return new XLog2StochasticLanguageConverterImpl(XLogSimplifier.getInstance(classifier, activityFactory),
                SimplifiedLog2StochasticLanguageConverter.getInstance());
    }
}
