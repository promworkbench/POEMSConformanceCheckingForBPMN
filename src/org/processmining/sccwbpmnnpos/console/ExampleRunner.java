package org.processmining.sccwbpmnnpos.console;

import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.classification.XEventNameClassifier;
import org.deckfour.xes.model.XLog;
import org.processmining.models.graphbased.directed.transitionsystem.ReachabilityGraph;
import org.processmining.sccwbpmnnpos.algorithms.conformance_checking.poems.POEMSConformanceChecking;
import org.processmining.sccwbpmnnpos.algorithms.conformance_checking.poems.POEMSConformanceCheckingEMSC24Adapter;
import org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.stochastic.statespace.StochasticBpmn2POReachabilityGraphConverter;
import org.processmining.sccwbpmnnpos.algorithms.inputs.reachability_graph.stochastic.analyzer.StochasticReachabilityGraphStaticAnalysis;
import org.processmining.sccwbpmnnpos.algorithms.inputs.reachability_graph.stochastic.analyzer.StochasticReachabilityGraphStaticAnalyzer;
import org.processmining.sccwbpmnnpos.algorithms.inputs.stochastic_language.StochasticLanguageGenerator;
import org.processmining.sccwbpmnnpos.algorithms.inputs.stochastic_language.stopping.NumElementsStochasticLanguageGeneratorStopper;
import org.processmining.sccwbpmnnpos.algorithms.inputs.stochastic_language.stopping.StochasticLanguageGeneratorStopper;
import org.processmining.sccwbpmnnpos.algorithms.utils.stochastics.sampling.strategy.graph.StochasticGraphPathSamplingStrategy;
import org.processmining.sccwbpmnnpos.models.bpmn.conformance.result.POEMSConformanceCheckingResult;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.BpmnMarking;
import org.processmining.sccwbpmnnpos.models.bpmn.stochastic.language.trace.BpmnStochasticPOTraceLanguage;
import org.processmining.sccwbpmnnpos.models.log.stochastic.language.EventLogStochasticTOTraceLanguage;
import org.processmining.sccwbpmnnpos.models.utils.activity.factory.ActivityFactory;
import org.processmining.sccwbpmnnpos.utils.log.XLogReader;
import org.processmining.stochasticbpmn.algorithms.diagram.reader.StochasticBPMNDiagramReader;
import org.processmining.stochasticbpmn.algorithms.reader.ObjectReader;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticBPMNDiagram;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExampleRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExampleRunner.class);

    public static void main(String[] args) {
        // Parameters
        StochasticGraphPathSamplingStrategy.GraphSamplingType defaultType = StochasticGraphPathSamplingStrategy.getDefaultType();
        StochasticLanguageGeneratorStopper stopper = new NumElementsStochasticLanguageGeneratorStopper(10000);
        XEventClassifier defaultClassifier = new XEventNameClassifier();


        ObjectReader<String, XLog> logReader = XLogReader.fromFileName();
        ActivityFactory activityFactory = ActivityFactory.getInstance();
        StochasticBpmn2POReachabilityGraphConverter sbpmn2Rg = StochasticBpmn2POReachabilityGraphConverter.getInstance();
        StochasticReachabilityGraphStaticAnalyzer<BpmnMarking> rgStaticAnalyzer = StochasticReachabilityGraphStaticAnalyzer.getInstance(BpmnMarking.class);
        StochasticLanguageGenerator languageGenerator = StochasticLanguageGenerator.getInstance(activityFactory, defaultClassifier, defaultType, stopper);
        ObjectReader<String, StochasticBPMNDiagram> diagramReader = StochasticBPMNDiagramReader.fromFileName();
        POEMSConformanceChecking poemsConformanceCheckingEmsc24Adapter = new POEMSConformanceCheckingEMSC24Adapter(activityFactory);

        try {
            final XLog log = logReader.read
                    ("/home/aleks/Documents/DataResources/ProcessMining/Logs/Handling of Compensation Requests/Handling of Compensation Requests.xes");
            StochasticBPMNDiagram diagram = diagramReader.read("/home/aleks/Documents/Learn/Playground/obsidianTest/alkuzman/Research/Concepts/Process Management/Process Mining/Process Models/BPMN/Stochastic/Instances/Instance - Stochastic BPMN - rtfm_IMf02_ABE 1.bpmn");
            ReachabilityGraph rg = sbpmn2Rg.convert(diagram);
            StochasticReachabilityGraphStaticAnalysis<BpmnMarking> rgAnalysisResult = rgStaticAnalyzer.analyze(rg);
            ReachabilityGraph newRg = rgAnalysisResult.getFixedReachabilityGraph();
            EventLogStochasticTOTraceLanguage stochasticLogLanguage = languageGenerator.trace(log);
            BpmnStochasticPOTraceLanguage stochasticModelLanguage = languageGenerator.poTrace(newRg);
            POEMSConformanceCheckingResult poemsConformanceCheckingResult = poemsConformanceCheckingEmsc24Adapter.calculateConformance(stochasticModelLanguage, stochasticLogLanguage);
            System.out.println(poemsConformanceCheckingResult);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
