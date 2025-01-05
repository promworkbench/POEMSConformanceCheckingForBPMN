package org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.reachability_graph.stochastic.language.path;

import org.processmining.models.graphbased.directed.transitionsystem.ReachabilityGraph;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.bpmn.statespace.path.BpmnPOReachabilityGraphPathConstructor;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.reachability_graph.stochastic.analyzer.StochasticReachabilityGraphStaticAnalysis;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.reachability_graph.stochastic.analyzer.StochasticReachabilityGraphStaticAnalyzer;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.reachability_graph.stochastic.language.path.SPORG2StochasticPathLanguageConverterImpl.IntermediateStochasticTransition;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.utils.stochastics.sampling.stopping.factory.SamplingStoppingCriterionProvider;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.utils.stochastics.sampling.strategy.transition.TransitionSamplingStrategy;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.marking.BpmnMarking;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.stochastic.language.path.BpmnStochasticPOPathLanguage;

public class SPORG2StochasticPathLanguageConverterAnalyzerDecorator implements SPORG2StochasticPathLanguageConverter {
    private final StochasticReachabilityGraphStaticAnalyzer<BpmnMarking> analyzer;
    private final SamplingStoppingCriterionProvider stoppingCriterionProvider;
    private final TransitionSamplingStrategy<IntermediateStochasticTransition> samplingStrategy;
    private final BpmnPOReachabilityGraphPathConstructor pathConstructor;

    public SPORG2StochasticPathLanguageConverterAnalyzerDecorator(
            StochasticReachabilityGraphStaticAnalyzer<BpmnMarking> analyzer,
            SamplingStoppingCriterionProvider stoppingCriterionProvider,
            TransitionSamplingStrategy<IntermediateStochasticTransition> samplingStrategy,
            BpmnPOReachabilityGraphPathConstructor pathConstructor
    ) {
        this.analyzer = analyzer;
        this.stoppingCriterionProvider = stoppingCriterionProvider;
        this.samplingStrategy = samplingStrategy;
        this.pathConstructor = pathConstructor;
    }


    @Override
    public BpmnStochasticPOPathLanguage convert(ReachabilityGraph reachabilityGraph) {
        StochasticReachabilityGraphStaticAnalysis<BpmnMarking> rgAnalysis = analyzer.analyze(reachabilityGraph);
        SPORG2StochasticPathLanguageConverterImpl converter = new SPORG2StochasticPathLanguageConverterImpl(
                samplingStrategy,
                stoppingCriterionProvider.provide(rgAnalysis.getProbabilityToComplete()),
                pathConstructor
        );
        return converter.convert(rgAnalysis.getFixedReachabilityGraph());
    }
}
