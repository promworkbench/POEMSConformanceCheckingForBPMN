package org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.stochastic_language;

import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.classification.XEventNameClassifier;
import org.deckfour.xes.model.XLog;
import org.processmining.models.graphbased.directed.transitionsystem.ReachabilityGraph;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.bpmn.statespace.BpmnNoOptionToCompleteException;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.bpmn.statespace.BpmnUnboundedException;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.bpmn.stochastic.statespace.StochasticBpmn2POReachabilityGraphConverter;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.bpmn.stochastic.statespace.language.path.StochasticBpmnPORG2StochasticPathLanguageConverter;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.bpmn.stochastic.statespace.language.trace.StochasticBpmnPORG2StochasticTraceLanguageConverter;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.log.stohastic_language.xlog.Xlog2StochasticTraceLanguageConverter;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.reachability_graph.stochastic.analyzer.StochasticReachabilityGraphStaticAnalyzer;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.utils.stochastics.sampling.stopping.SamplingStoppingCriterion;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.utils.stochastics.sampling.strategy.graph.TransitionSamplingStrategy;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.utils.stochastics.sampling.strategy.graph.TansitionSamplingStrategyType;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.marking.BpmnMarking;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.stochastic.execution.node.factory.ExecutableStochasticBpmnNodeFactory;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.stochastic.language.path.BpmnStochasticPOPathLanguage;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.stochastic.language.trace.BpmnStochasticPOTraceLanguage;
import org.processmining.poemsconformancecheckingforbpmn.models.log.stochastic.language.EventLogStochasticTOTraceLanguage;
import org.processmining.poemsconformancecheckingforbpmn.models.utils.activity.factory.ActivityFactory;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticBPMNDiagram;

public interface StochasticLanguageGenerator {
    static StochasticLanguageGenerator getInstance(ActivityFactory activityFactory) {
        return getInstance(activityFactory, new XEventNameClassifier(), TransitionSamplingStrategy.getDefaultType(), SamplingStoppingCriterion.getInstance(), 1000);
    }

    static StochasticLanguageGenerator getInstance(ActivityFactory activityFactory, XEventClassifier defaultClassifier, TansitionSamplingStrategyType samplingStrategy, SamplingStoppingCriterion stopper, int maxPathLength) {
        ExecutableStochasticBpmnNodeFactory nodeFactory = ExecutableStochasticBpmnNodeFactory.getInstance();
        StochasticBpmn2POReachabilityGraphConverter sbpmn2Graph =
                StochasticBpmn2POReachabilityGraphConverter.getInstance(nodeFactory);

        Xlog2StochasticTraceLanguageConverter log2Trace =
                Xlog2StochasticTraceLanguageConverter.getInstance(defaultClassifier, activityFactory);
        StochasticBpmnPORG2StochasticPathLanguageConverter graph2POPath =
                StochasticBpmnPORG2StochasticPathLanguageConverter.getInstance(samplingStrategy, stopper, nodeFactory, maxPathLength);
        StochasticBpmnPORG2StochasticTraceLanguageConverter graph2POTrace =
                StochasticBpmnPORG2StochasticTraceLanguageConverter.getInstance(activityFactory, samplingStrategy, stopper, maxPathLength);

        return new StochasticLanguageGeneratorImpl(log2Trace, sbpmn2Graph, graph2POPath, graph2POTrace,
                StochasticReachabilityGraphStaticAnalyzer.getInstance(BpmnMarking.class));
    }

    static StochasticLanguageGenerator getInstance(XEventClassifier defaultClassifier, TansitionSamplingStrategyType samplingStrategy, SamplingStoppingCriterion stopper, int maxPathLength) {
        return getInstance(ActivityFactory.getInstance(), defaultClassifier, samplingStrategy, stopper, maxPathLength);
    }

    static StochasticLanguageGenerator getInstance(TansitionSamplingStrategyType samplingStrategy, SamplingStoppingCriterion stopper, int maxPathLength) {
        return getInstance(new XEventNameClassifier(), samplingStrategy, stopper, maxPathLength);
    }

    static StochasticLanguageGenerator getInstance() {
        return getInstance(TransitionSamplingStrategy.getDefaultType(), SamplingStoppingCriterion.getInstance(), 1000);
    }

    BpmnStochasticPOPathLanguage poPath(final StochasticBPMNDiagram diagram) throws BpmnNoOptionToCompleteException,
            BpmnUnboundedException;

    BpmnStochasticPOTraceLanguage poTrace(final StochasticBPMNDiagram diagram) throws BpmnNoOptionToCompleteException
            , BpmnUnboundedException;

    BpmnStochasticPOPathLanguage poPath(final ReachabilityGraph graph);

    BpmnStochasticPOTraceLanguage poTrace(final ReachabilityGraph graph);

    EventLogStochasticTOTraceLanguage trace(final XLog log);
}
