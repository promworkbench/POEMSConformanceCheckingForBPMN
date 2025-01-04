package org.processmining.poemsconformancecheckingforbpmn.console;

import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.classification.XEventNameClassifier;
import org.deckfour.xes.model.XLog;
import org.processmining.models.graphbased.directed.transitionsystem.ReachabilityGraph;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.conformance_checking.poems.POEMSConformanceChecking;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.conformance_checking.poems.POEMSConformanceCheckingEMSC24Adapter;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.bpmn.stochastic.statespace.StochasticBpmn2POReachabilityGraphConverter;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.reachability_graph.stochastic.analyzer.StochasticReachabilityGraphStaticAnalysis;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.reachability_graph.stochastic.analyzer.StochasticReachabilityGraphStaticAnalyzer;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.stochastic_language.StochasticLanguageGenerator;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.utils.stochastics.sampling.stopping.SampleSizeStoppingCriterion;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.utils.stochastics.sampling.stopping.SamplingStoppingCriterion;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.utils.stochastics.sampling.strategy.graph.TansitionSamplingStrategyType;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.utils.stochastics.sampling.strategy.graph.TransitionSamplingStrategy;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.conformance.result.POEMSConformanceCheckingResult;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.marking.BpmnMarking;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.stochastic.language.trace.BpmnStochasticPOTraceLanguage;
import org.processmining.poemsconformancecheckingforbpmn.models.log.stochastic.language.EventLogStochasticTOTraceLanguage;
import org.processmining.poemsconformancecheckingforbpmn.models.utils.activity.factory.ActivityFactory;
import org.processmining.poemsconformancecheckingforbpmn.utils.log.XLogReader;
import org.processmining.stochasticbpmn.algorithms.diagram.reader.StochasticBPMNDiagramReader;
import org.processmining.stochasticbpmn.algorithms.reader.ObjectReader;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticBPMNDiagram;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExampleRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExampleRunner.class);

    public static void main(String[] args) {
        // Parameters
        TansitionSamplingStrategyType defaultType = TransitionSamplingStrategy.getDefaultType();
        SamplingStoppingCriterion stopper = new SampleSizeStoppingCriterion(10000);
        XEventClassifier defaultClassifier = new XEventNameClassifier();


        ObjectReader<String, XLog> logReader = XLogReader.fromFileName();
        ActivityFactory activityFactory = ActivityFactory.getInstance();
        StochasticBpmn2POReachabilityGraphConverter sbpmn2Rg = StochasticBpmn2POReachabilityGraphConverter.getInstance();
        StochasticReachabilityGraphStaticAnalyzer<BpmnMarking> rgStaticAnalyzer = StochasticReachabilityGraphStaticAnalyzer.getInstance(BpmnMarking.class);
        StochasticLanguageGenerator languageGenerator = StochasticLanguageGenerator.getInstance(activityFactory, defaultClassifier, defaultType, stopper, 1000);
        ObjectReader<String, StochasticBPMNDiagram> diagramReader = StochasticBPMNDiagramReader.fromFileName();
        POEMSConformanceChecking poemsConformanceCheckingEmsc24Adapter = new POEMSConformanceCheckingEMSC24Adapter(activityFactory);

        try {
            final XLog log = logReader.read
                    ("/home/aleks/Documents/DataResources/ProcessMining/Logs/POEMS Evaluation Example/Log3.xes");
            StochasticBPMNDiagram diagram = diagramReader.read("/home/aleks/Documents/Learn/Playground/obsidianTest/alkuzman/Research/Concepts/Process Management/Process Mining/Process Models/BPMN/Specializations/Stochastic/Instances/POEMS Evaluation Examples/flower.bpmn");
            ReachabilityGraph rg = sbpmn2Rg.convert(diagram);
            StochasticReachabilityGraphStaticAnalysis<BpmnMarking> rgAnalysisResult = rgStaticAnalyzer.analyze(rg);
            ReachabilityGraph newRg = rgAnalysisResult.getFixedReachabilityGraph();
            EventLogStochasticTOTraceLanguage stochasticLogLanguage = languageGenerator.trace(log);
            BpmnStochasticPOTraceLanguage stochasticModelLanguage = languageGenerator.poTrace(newRg);
            System.out.println(stochasticLogLanguage);
            System.out.println(stochasticModelLanguage.toGraphViz());
            POEMSConformanceCheckingResult poemsConformanceCheckingResult = poemsConformanceCheckingEmsc24Adapter.calculateConformance(stochasticModelLanguage, stochasticLogLanguage);
            System.out.println(poemsConformanceCheckingResult);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
