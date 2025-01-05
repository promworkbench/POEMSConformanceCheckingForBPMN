package org.processmining.poemsconformancecheckingforbpmn.algorithms.conformance_checking.poems.bpmn;

import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.classification.XEventNameClassifier;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.conformance_checking.BPMNStochasticConformanceChecking;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.conformance_checking.poems.POEMSConformanceChecking;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.conformance_checking.poems.POEMSConformanceCheckingEMSC24Adapter;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.stochastic_language.StochasticLanguageGenerator;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.utils.stochastics.sampling.stopping.factory.SamplingStoppingCriterionProvider;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.utils.stochastics.sampling.strategy.transition.TansitionSamplingStrategyType;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.utils.stochastics.sampling.strategy.transition.TransitionSamplingStrategy;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.conformance.result.POEMSConformanceCheckingResult;
import org.processmining.poemsconformancecheckingforbpmn.models.utils.activity.factory.ActivityFactory;

import java.util.function.Supplier;

public interface BpmnPoemsConformanceChecking extends BPMNStochasticConformanceChecking<POEMSConformanceCheckingResult> {
    static BpmnPoemsConformanceChecking getInstance() {
        return getInstance(
                new XEventNameClassifier(),
                TransitionSamplingStrategy.getDefaultType(),
                SamplingStoppingCriterionProvider.getInstance(),
                () -> false
        );
    }

    static BpmnPoemsConformanceChecking getInstance(
            XEventClassifier classifier,
            TansitionSamplingStrategyType samplingStrategyType,
            SamplingStoppingCriterionProvider stoppingCriterionProvider,
            Supplier<Boolean> canceller
    ) {
        ActivityFactory activityFactory = ActivityFactory.getInstance();
        StochasticLanguageGenerator languageGenerator = StochasticLanguageGenerator.getInstance(
                activityFactory,
                classifier,
                samplingStrategyType,
                stoppingCriterionProvider
        );
        POEMSConformanceChecking conformanceChecker = new POEMSConformanceCheckingEMSC24Adapter(activityFactory, canceller);
        return new BpmnPoemsConformanceCheckingImpl(
                languageGenerator,
                conformanceChecker
        );
    }
}
