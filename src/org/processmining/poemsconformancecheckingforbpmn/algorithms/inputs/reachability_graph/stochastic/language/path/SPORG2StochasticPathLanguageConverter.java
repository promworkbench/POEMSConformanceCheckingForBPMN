package org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.reachability_graph.stochastic.language.path;

import org.processmining.models.graphbased.directed.transitionsystem.ReachabilityGraph;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.bpmn.statespace.path.BpmnPOReachabilityGraphPathConstructor;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.reachability_graph.stochastic.analyzer.StochasticReachabilityGraphStaticAnalyzer;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.utils.stochastics.sampling.stopping.SamplingStoppingCriterion;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.utils.stochastics.sampling.stopping.factory.SamplingStoppingCriterionProvider;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.utils.stochastics.sampling.strategy.transition.TansitionSamplingStrategyType;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.utils.stochastics.sampling.strategy.transition.TransitionSamplingStrategy;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.marking.BpmnMarking;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.stochastic.execution.node.factory.ExecutableStochasticBpmnNodeFactory;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.stochastic.language.path.BpmnStochasticPOPathLanguage;

public interface SPORG2StochasticPathLanguageConverter {
    static SPORG2StochasticPathLanguageConverter getInstance(
            TansitionSamplingStrategyType type,
            SamplingStoppingCriterionProvider stoppingCriterionProvider
    ) {
        return getInstance(
                type,
                stoppingCriterionProvider,
                ExecutableStochasticBpmnNodeFactory.getInstance()
        );
    }

    static SPORG2StochasticPathLanguageConverter getInstance(
            TansitionSamplingStrategyType type,
            SamplingStoppingCriterionProvider stoppingCriterionProvider,
            ExecutableStochasticBpmnNodeFactory nodeFactory
    ) {
        BpmnPOReachabilityGraphPathConstructor pathConstructor =
                BpmnPOReachabilityGraphPathConstructor.getInstance(nodeFactory);
        return new SPORG2StochasticPathLanguageConverterAnalyzerDecorator(
                StochasticReachabilityGraphStaticAnalyzer.getInstance(BpmnMarking.class),
                stoppingCriterionProvider,
                TransitionSamplingStrategy.getInstance(type),
                pathConstructor
        );
    }

    static SPORG2StochasticPathLanguageConverter getInstance() {
        return getInstance(
                TransitionSamplingStrategy.getDefaultType(),
                SamplingStoppingCriterion.getInstance()
        );
    }

    static SPORG2StochasticPathLanguageConverter getInstance(
            TansitionSamplingStrategyType type,
            SamplingStoppingCriterion stopper
    ) {
        return getInstance(
                type,
                stopper,
                ExecutableStochasticBpmnNodeFactory.getInstance()
        );
    }

    static SPORG2StochasticPathLanguageConverter getInstance(
            TansitionSamplingStrategyType type,
            SamplingStoppingCriterion stopper,
            ExecutableStochasticBpmnNodeFactory nodeFactory
    ) {
        BpmnPOReachabilityGraphPathConstructor pathConstructor =
                BpmnPOReachabilityGraphPathConstructor.getInstance(nodeFactory);
        return new SPORG2StochasticPathLanguageConverterImpl(
                TransitionSamplingStrategy.getInstance(type),
                stopper,
                pathConstructor
        );
    }

    BpmnStochasticPOPathLanguage convert(ReachabilityGraph transitionSystem);
}
