package org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.stochastic_language;

import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.classification.XEventNameClassifier;
import org.deckfour.xes.model.XLog;
import org.processmining.models.graphbased.directed.transitionsystem.ReachabilityGraph;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.bpmn.statespace.BpmnNoOptionToCompleteException;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.bpmn.statespace.BpmnUnboundedException;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.bpmn.stochastic.statespace.StochasticBpmn2POReachabilityGraphConverter;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.log.stohastic_language.xlog.Xlog2StochasticTraceLanguageConverter;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.reachability_graph.stochastic.analyzer.StochasticReachabilityGraphStaticAnalyzer;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.reachability_graph.stochastic.language.path.SPORG2StochasticPathLanguageConverter;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.reachability_graph.stochastic.language.trace.SPORG2StochasticTraceLanguageConverter;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.utils.stochastics.sampling.stopping.SamplingStoppingCriterion;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.utils.stochastics.sampling.stopping.factory.SamplingStoppingCriterionProvider;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.utils.stochastics.sampling.strategy.transition.TansitionSamplingStrategyType;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.utils.stochastics.sampling.strategy.transition.TransitionSamplingStrategy;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.marking.BpmnMarking;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.stochastic.execution.node.factory.ExecutableStochasticBpmnNodeFactory;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.stochastic.language.path.BpmnStochasticPOPathLanguage;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.stochastic.language.trace.BpmnStochasticPOTraceLanguage;
import org.processmining.poemsconformancecheckingforbpmn.models.log.stochastic.language.EventLogStochasticTOTraceLanguage;
import org.processmining.poemsconformancecheckingforbpmn.models.utils.activity.factory.ActivityFactory;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticBPMNDiagram;
import org.processmining.stochasticbpmn.models.stochastic.Probability;

public interface StochasticLanguageGenerator {
    static StochasticLanguageGenerator getInstance(ActivityFactory activityFactory) {
        SamplingStoppingCriterionProvider provider = new SamplingStoppingCriterionProvider() {
            @Override
            public SamplingStoppingCriterion provide(Probability populationProbability) {
                return SamplingStoppingCriterion.getInstance();
            }
        };
        return getInstance(
                activityFactory,
                new XEventNameClassifier(),
                TransitionSamplingStrategy.getDefaultType(),
                provider
        );
    }

    static StochasticLanguageGenerator getInstance(
            ActivityFactory activityFactory,
            XEventClassifier defaultClassifier,
            TansitionSamplingStrategyType samplingStrategy,
            SamplingStoppingCriterionProvider stoppingCriterionProvider
    ) {
        ExecutableStochasticBpmnNodeFactory nodeFactory = ExecutableStochasticBpmnNodeFactory.getInstance();
        StochasticBpmn2POReachabilityGraphConverter sbpmn2Graph =
                StochasticBpmn2POReachabilityGraphConverter.getInstance(nodeFactory);

        Xlog2StochasticTraceLanguageConverter log2Trace =
                Xlog2StochasticTraceLanguageConverter.getInstance(
                        defaultClassifier,
                        activityFactory
                );
        SPORG2StochasticPathLanguageConverter graph2POPath =
                SPORG2StochasticPathLanguageConverter.getInstance(
                        samplingStrategy,
                        stoppingCriterionProvider,
                        nodeFactory
                );
        SPORG2StochasticTraceLanguageConverter graph2POTrace =
                SPORG2StochasticTraceLanguageConverter.getInstance(
                        activityFactory,
                        samplingStrategy,
                        stoppingCriterionProvider
                );

        StochasticReachabilityGraphStaticAnalyzer<BpmnMarking> analyzer =
                StochasticReachabilityGraphStaticAnalyzer.getInstance(BpmnMarking.class);
        return new StochasticLanguageGeneratorImpl(
                log2Trace,
                sbpmn2Graph,
                graph2POPath,
                graph2POTrace
        );
    }

    static StochasticLanguageGenerator getInstance() {
        SamplingStoppingCriterionProvider provider = new SamplingStoppingCriterionProvider() {
            @Override
            public SamplingStoppingCriterion provide(Probability populationProbability) {
                return SamplingStoppingCriterion.getInstance();
            }
        };
        return getInstance(
                TransitionSamplingStrategy.getDefaultType(),
                provider
        );
    }

    static StochasticLanguageGenerator getInstance(
            TansitionSamplingStrategyType samplingStrategy,
            SamplingStoppingCriterionProvider stopper
    ) {
        return getInstance(
                new XEventNameClassifier(),
                samplingStrategy,
                stopper
        );
    }

    static StochasticLanguageGenerator getInstance(
            XEventClassifier defaultClassifier,
            TansitionSamplingStrategyType samplingStrategy,
            SamplingStoppingCriterionProvider stopper
    ) {
        return getInstance(
                ActivityFactory.getInstance(),
                defaultClassifier,
                samplingStrategy,
                stopper
        );
    }

    BpmnStochasticPOPathLanguage poPath(final StochasticBPMNDiagram diagram) throws BpmnNoOptionToCompleteException,
            BpmnUnboundedException;

    BpmnStochasticPOTraceLanguage poTrace(final StochasticBPMNDiagram diagram) throws BpmnNoOptionToCompleteException
            , BpmnUnboundedException;

    BpmnStochasticPOPathLanguage poPath(final ReachabilityGraph graph);

    BpmnStochasticPOTraceLanguage poTrace(final ReachabilityGraph graph);

    EventLogStochasticTOTraceLanguage trace(final XLog log);
}
