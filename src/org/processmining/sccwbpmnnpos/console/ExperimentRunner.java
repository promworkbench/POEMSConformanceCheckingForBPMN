package org.processmining.sccwbpmnnpos.console;

import org.deckfour.xes.classification.XEventNameClassifier;
import org.deckfour.xes.model.XLog;
import org.processmining.models.graphbased.directed.transitionsystem.ReachabilityGraph;
import org.processmining.plugins.InductiveMiner.Pair;
import org.processmining.sccwbpmnnpos.algorithms.conformance_checking.poems.POEMSConformanceChecking;
import org.processmining.sccwbpmnnpos.algorithms.conformance_checking.poems.POEMSConformanceCheckingEMSC24Adapter;
import org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.statespace.Bpmn2POReachabilityGraphConverter;
import org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.statespace.BpmnNoOptionToCompleteException;
import org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.statespace.BpmnUnboundedException;
import org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.stochastic.statespace.StochasticBpmn2POReachabilityGraphConverter;
import org.processmining.sccwbpmnnpos.algorithms.inputs.reachability_graph.stochastic.StochasticReachabilityGraphStaticAnalysis;
import org.processmining.sccwbpmnnpos.algorithms.inputs.reachability_graph.stochastic.StochasticReachabilityGraphStaticAnalyzer;
import org.processmining.sccwbpmnnpos.algorithms.inputs.stochastic_language.StochasticLanguageGenerator;
import org.processmining.sccwbpmnnpos.algorithms.inputs.stochastic_language.stopping.ProbabilityMassStochasticLanguageGeneratorStopper;
import org.processmining.sccwbpmnnpos.algorithms.utils.stochastics.sampling.strategy.graph.StochasticGraphPathSamplingStrategy.GraphSamplingType;
import org.processmining.sccwbpmnnpos.models.bpmn.conformance.result.POEMSConformanceCheckingResult;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.BpmnMarking;
import org.processmining.sccwbpmnnpos.models.bpmn.stochastic.language.trace.BpmnStochasticPOTraceLanguage;
import org.processmining.sccwbpmnnpos.models.log.stochastic.language.EventLogStochasticTOTraceLanguage;
import org.processmining.sccwbpmnnpos.models.utils.activity.factory.ActivityFactory;
import org.processmining.sccwbpmnnpos.utils.log.XLogReader;
import org.processmining.stochasticbpmn.algorithms.diagram.reader.StochasticBPMNDiagramReader;
import org.processmining.stochasticbpmn.algorithms.reader.ObjectReader;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticBPMNDiagram;
import org.processmining.stochasticbpmn.models.stochastic.Probability;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Objects;

public class ExperimentRunner {
    private static final Logger logger = LoggerFactory.getLogger(ExampleRunner.class);
    private final ObjectReader<File, StochasticBPMNDiagram> modelReader;
    private final ObjectReader<File, XLog> logReader;
    private final StochasticBpmn2POReachabilityGraphConverter sbpmn2Rg;
    private final StochasticReachabilityGraphStaticAnalyzer<BpmnMarking> rgAnalyzer;

    public ExperimentRunner() {
        this.modelReader = StochasticBPMNDiagramReader.fromFile();
        this.logReader = XLogReader.fromFile();
        this.sbpmn2Rg = StochasticBpmn2POReachabilityGraphConverter.getInstance();
        this.rgAnalyzer = StochasticReachabilityGraphStaticAnalyzer.getInstance(BpmnMarking.class);
    }

    public static void main(String[] args) {
        new ExperimentRunner().runExperiments("/home/aleks/Documents/DataResources/ProcessMining/Logs/",
                "/home/aleks/Documents/Learn/Playground/obsidianTest/alkuzman/Research/Concepts/Process " +
                        "Management/Process Mining/Process Models/BPMN/Stochastic/Instances/Logs/",
                "/home/aleks/Documents/Learn/Playground/obsidianTest/alkuzman/Projects/Stochastic Conformance " +
                        "Checking with BPMN");
    }

    private void runExperiments(String logsFolderPath, String modelFolderPath, String resultFolderPath) {
        File logsFolder = new File(logsFolderPath);
        if (!logsFolder.exists() || !logsFolder.isDirectory()) {
            return;
        }
        File[] logFolders = logsFolder.listFiles();
        if (Objects.isNull(logFolders)) {
            return;
        }
        for (File logFolder : logFolders) {
            String modelsForLogFolderPath = modelFolderPath + logFolder.getName();
            File modelsForLogFolder = new File(modelsForLogFolderPath);
            if (!modelsForLogFolder.exists() || !modelsForLogFolder.isDirectory()) {
                continue;
            }
            File[] modelVariantForLogFolders = modelsForLogFolder.listFiles();
            if (Objects.isNull(modelVariantForLogFolders)) {
                continue;
            }
            File[] logFiles = logFolder.listFiles();
            if (Objects.isNull(logFiles)) {
                continue;
            }
            XLog log = null;
            for (File logFile : logFiles) {
                String[] logNameParts = logFile.getName().split("\\.");
                if ("xes".equals(logNameParts[logNameParts.length - 1])) {
                    try {
                        log = logReader.read(logFile);
                        System.out.println(logFolder.getName());
                    } catch (Exception e) {
                        logger.error(String.format("Failure to read log from %s/%s", logFile.getPath(),
                                logFile.getName()), e);
                    }
                }
            }
            if (Objects.isNull(log)) {
                continue;
            }

            ActivityFactory activityFactory = ActivityFactory.getInstance();
            for (File modelVariantForLogFolder : modelVariantForLogFolders) {
                File[] modelVariantForLogFiles = modelVariantForLogFolder.listFiles();
                if (Objects.isNull(modelVariantForLogFiles)) {
                    System.out.printf("No models for %s", logFolder.getName());
                    continue;
                }
                for (File modelVariantForLogFile : modelVariantForLogFiles) {
                    String[] logNameParts = modelVariantForLogFile.getName().split("\\.");
                    if ("bpmn".equals(logNameParts[logNameParts.length - 1])) {
                        try {
                            StochasticBPMNDiagram model = modelReader.read(modelVariantForLogFile);
                            Pair<POEMSConformanceCheckingResult,
                                    StochasticReachabilityGraphStaticAnalysis<BpmnMarking>> result =
                                    runExperiments(log, model, activityFactory);
                            System.out.printf("%s: %s - %s\n", modelVariantForLogFolder.getName(), result.getA(), result.getB());
                        } catch (Exception e) {
                            logger.error(String.format("Failure to read log from %s/%s",
                                    modelVariantForLogFile.getPath(), modelVariantForLogFile.getName()), e);
                        }
                    }
                }
            }
        }
    }

    private Pair<POEMSConformanceCheckingResult, StochasticReachabilityGraphStaticAnalysis<BpmnMarking>> runExperiments(XLog log, StochasticBPMNDiagram bpmnDiagram,
                                                                                                                        ActivityFactory activityFactory) {
        try {
            ReachabilityGraph rg = sbpmn2Rg.convert(bpmnDiagram);
            StochasticReachabilityGraphStaticAnalysis<BpmnMarking> rgStaticAnalysis = rgAnalyzer.analyze(rg);
            ReachabilityGraph fixedRg = rgStaticAnalysis.getFixedReachabilityGraph();
            Probability requiredProbability =
                    rgStaticAnalysis.getProbabilityToComplete().subtract(Probability.of(0.0001));

            StochasticLanguageGenerator languageGenerator = StochasticLanguageGenerator.getInstance(activityFactory,
                    new XEventNameClassifier(), GraphSamplingType.MOST_PROBABLE,
                    new ProbabilityMassStochasticLanguageGeneratorStopper(requiredProbability));
            BpmnStochasticPOTraceLanguage modelLanguage = languageGenerator.poTrace(fixedRg);
            EventLogStochasticTOTraceLanguage logLanguage = languageGenerator.trace(log);

            POEMSConformanceChecking conformanceChecking = new POEMSConformanceCheckingEMSC24Adapter(activityFactory);
            POEMSConformanceCheckingResult result = conformanceChecking.calculateConformance(modelLanguage,
                    logLanguage);
            return Pair.of(result, rgStaticAnalysis);
        } catch (BpmnUnboundedException | BpmnNoOptionToCompleteException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
