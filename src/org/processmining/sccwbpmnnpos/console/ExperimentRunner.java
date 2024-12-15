package org.processmining.sccwbpmnnpos.console;

import org.deckfour.xes.classification.XEventNameClassifier;
import org.deckfour.xes.model.XLog;
import org.processmining.models.graphbased.directed.transitionsystem.ReachabilityGraph;
import org.processmining.plugins.InductiveMiner.Pair;
import org.processmining.sccwbpmnnpos.algorithms.conformance_checking.poems.POEMSConformanceChecking;
import org.processmining.sccwbpmnnpos.algorithms.conformance_checking.poems.POEMSConformanceCheckingEMSC24Adapter;
import org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.statespace.BpmnNoOptionToCompleteException;
import org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.statespace.BpmnUnboundedException;
import org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.stochastic.statespace.StochasticBpmn2POReachabilityGraphConverter;
import org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.stochastic.statespace.language.trace.StochasticBpmnPORG2StochasticTraceLanguageConverter;
import org.processmining.sccwbpmnnpos.algorithms.inputs.log.simplifier.XLogSimplifier;
import org.processmining.sccwbpmnnpos.algorithms.inputs.log.stohastic_language.simplified.SimplifiedLog2StochasticLanguageConverter;
import org.processmining.sccwbpmnnpos.algorithms.inputs.log.stohastic_language.xlog.Xlog2StochasticTraceLanguageConverter;
import org.processmining.sccwbpmnnpos.algorithms.inputs.reachability_graph.stochastic.analyzer.StochasticReachabilityGraphStaticAnalysis;
import org.processmining.sccwbpmnnpos.algorithms.inputs.reachability_graph.stochastic.analyzer.StochasticReachabilityGraphStaticAnalyzer;
import org.processmining.sccwbpmnnpos.algorithms.inputs.stochastic_language.StochasticLanguageGenerator;
import org.processmining.sccwbpmnnpos.algorithms.inputs.stochastic_language.stopping.ProbabilityMassStochasticLanguageGeneratorStopper;
import org.processmining.sccwbpmnnpos.algorithms.utils.stochastics.sampling.strategy.graph.StochasticGraphPathSamplingStrategy.GraphSamplingType;
import org.processmining.sccwbpmnnpos.models.bpmn.conformance.result.POEMSConformanceCheckingResult;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.BpmnMarking;
import org.processmining.sccwbpmnnpos.models.bpmn.stochastic.language.trace.BpmnStochasticPOTraceLanguage;
import org.processmining.sccwbpmnnpos.models.log.EventLogTrace;
import org.processmining.sccwbpmnnpos.models.log.SimplifiedEventLog;
import org.processmining.sccwbpmnnpos.models.log.stochastic.language.EventLogStochasticTOTraceLanguage;
import org.processmining.sccwbpmnnpos.models.stochastic.language.StochasticLanguageEntry;
import org.processmining.sccwbpmnnpos.models.utils.activity.Activity;
import org.processmining.sccwbpmnnpos.models.utils.activity.factory.ActivityFactory;
import org.processmining.sccwbpmnnpos.utils.log.XLogReader;
import org.processmining.stochasticbpmn.algorithms.diagram.reader.StochasticBPMNDiagramFromSPNReader;
import org.processmining.stochasticbpmn.algorithms.diagram.reader.StochasticBPMNDiagramReader;
import org.processmining.stochasticbpmn.algorithms.reader.ObjectReader;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticBPMNDiagram;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticFlow;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticGateway;
import org.processmining.stochasticbpmn.models.stochastic.Probability;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ExperimentRunner {
    private static final Logger logger = LoggerFactory.getLogger(ExampleRunner.class);
    private final ObjectReader<File, XLog> logReader;
    private final SimplifiedLog2StochasticLanguageConverter slog2slConverter;
    private final ObjectReader<File, StochasticBPMNDiagram> modelReader;
    private final ObjectReader<File, StochasticBPMNDiagram> spnReader;
    private final StochasticBpmn2POReachabilityGraphConverter sbpmn2Rg;
    private final StochasticReachabilityGraphStaticAnalyzer<BpmnMarking> rgAnalyzer;

    public ExperimentRunner() {
        this.logReader = XLogReader.fromFile();
        this.slog2slConverter = SimplifiedLog2StochasticLanguageConverter.getInstance();
        this.modelReader = StochasticBPMNDiagramReader.fromFile();
        this.sbpmn2Rg = StochasticBpmn2POReachabilityGraphConverter.getInstance();
        this.rgAnalyzer = StochasticReachabilityGraphStaticAnalyzer.getInstance(BpmnMarking.class);
        this.spnReader = StochasticBPMNDiagramFromSPNReader.fromFile();
    }

    public static void main(String[] args) {
        File logsFolder = new File("/home/aleks/Documents/DataResources/ProcessMining/Logs/");
        File modelsFolder = new File("/home/aleks/Documents/DataResources/ProcessMining/StochasticPetriNets/Logs");
        File resultsFolder = new File("/home/aleks/Documents/Learn/Playground/obsidianTest/alkuzman/Projects/Stochastic Conformance " +
                "Checking with BPMN/Experiments");
        new ExperimentRunner().runExperiments(logsFolder, modelsFolder, resultsFolder);
    }

    private Map<String, File> getLogs(File logsFolder) {
        Map<String, File> logs = new HashMap<>();
        if (!logsFolder.exists() || !logsFolder.isDirectory()) {
            return logs;
        }
        File[] logFolders = logsFolder.listFiles();
        if (Objects.isNull(logFolders)) {
            return logs;
        }
        for (File logFolder : logFolders) {

            File[] logFiles = logFolder.listFiles();
            if (Objects.isNull(logFiles)) {
                continue;
            }

            for (File logFile : logFiles) {
                String[] logNameParts = logFile.getName().split("\\.");
                if ("xes".equals(logNameParts[logNameParts.length - 1])) {
                    logs.put(logFolder.getName(), logFile);
                }
            }
        }
        return logs;
    }

    private Map<String, Map<String, File>> getModels(File modelsFolder) {
        Map<String, Map<String, File>> models = new HashMap<>();
        if (!modelsFolder.exists() | !modelsFolder.isDirectory()) {
            return models;
        }

        File[] logFolders = modelsFolder.listFiles();
        if (Objects.isNull(logFolders)) {
            return models;
        }
        for (File logFolder : logFolders) {
            File[] modelVariantFolders = logFolder.listFiles();
            if (Objects.isNull(modelVariantFolders)) {
                continue;
            }
            Map<String, File> modelVariants = models.computeIfAbsent(logFolder.getName(), k -> new HashMap<>());
            for (File modelVariantFolder : modelVariantFolders) {
                File[] modelFiles = modelVariantFolder.listFiles();
                if (Objects.isNull(modelFiles)) {
                    continue;
                }
                for (File modelFile : modelFiles) {
                    String[] modelFileParts = modelFile.getName().split("\\.");
                    String extension = modelFileParts[modelFileParts.length - 1];
                    if (!(Objects.equals(extension, "bpmn") || Objects.equals(extension, "pnml"))) {
                        continue;
                    }
                    modelVariants.put(modelVariantFolder.getName(), modelFile);
                }
            }
        }

        return models;
    }

    private void runExperiments(File logsFolder, File modelsFolder, File resultsFolder) {
        Map<String, File> logFiles = getLogs(logsFolder);
        Map<String, Map<String, File>> modelLogFiles = getModels(modelsFolder);
        Set<String> uniqueModelVariants = modelLogFiles.values().stream().flatMap(m -> m.keySet().stream()).collect(Collectors.toSet());
        for (Map.Entry<String, File> logFile : logFiles.entrySet()) {
            Map<String, File> modelFiles = modelLogFiles.get(logFile.getKey());
            if (Objects.isNull(modelFiles) || modelFiles.isEmpty()) {
                continue;
            }
            try {
                XLog log = logReader.read(logFile.getValue());
                ActivityFactory activityFactory = ActivityFactory.getInstance();
                SimplifiedEventLog slog = XLogSimplifier.getInstance(new XEventNameClassifier(), activityFactory).simplify(log);
                EventLogStochasticTOTraceLanguage logLanguage = slog2slConverter.convert(slog);
                System.out.printf("%s - traces: %d, variants: %d\n", logFile.getKey(), slog.size(), logLanguage.size());
                for (Map.Entry<String, File> modelFileVariant : modelFiles.entrySet()) {
                    String[] modelFileParts = modelFileVariant.getValue().getName().split("\\.");
                    String extension = modelFileParts[modelFileParts.length - 1];
                    StochasticBPMNDiagram model;
                    if (Objects.equals(extension, "bpmn")) {
                        model = modelReader.read(modelFileVariant.getValue());
                    } else {
                        model = spnReader.read(modelFileVariant.getValue());
                    }
//                    model = modelReader.read(modelFileVariant.getValue());
                    System.out.println("Run: " + modelFileVariant.getValue().getParentFile().getName());
                    try {
                        ExperimentResult result =
                                runExperiments(logLanguage, model, activityFactory);
                        System.out.printf("%s: %s\n", modelFileVariant.getKey(), result);
                    } catch (Exception e) {
                        logger.error(String.format("Failure to execute %s", modelFileVariant.getValue().getParentFile().getName()), e);
                    }
                }
            } catch (Exception e) {
                logger.error(String.format("Failure to read log from %s", logFile.getValue().getPath()), e);
            }
        }
    }

    private ExperimentResult runExperiments(EventLogStochasticTOTraceLanguage logLanguage, StochasticBPMNDiagram bpmnDiagram, ActivityFactory activityFactory) {
        try {
            long startTime = System.currentTimeMillis();
            ReachabilityGraph rg = sbpmn2Rg.convert(bpmnDiagram);
            StochasticReachabilityGraphStaticAnalysis<BpmnMarking> rgStaticAnalysis = rgAnalyzer.analyze(rg);
            ReachabilityGraph fixedRg = rgStaticAnalysis.getFixedReachabilityGraph();
            Probability requiredProbability =
                    rgStaticAnalysis.getProbabilityToComplete().subtract(Probability.of(0.0001));

            int maxTraceSize = 0;
            for (StochasticLanguageEntry<Activity, EventLogTrace> trace :
                    logLanguage) {
                maxTraceSize = Math.max(maxTraceSize, trace.getElement().size());
            }
            StochasticBpmnPORG2StochasticTraceLanguageConverter languageGenerator =
                    StochasticBpmnPORG2StochasticTraceLanguageConverter.getInstance(activityFactory, GraphSamplingType.MOST_PROBABLE,
                    new ProbabilityMassStochasticLanguageGeneratorStopper(requiredProbability), maxTraceSize);
            BpmnStochasticPOTraceLanguage modelLanguage = languageGenerator.convert(fixedRg);

            POEMSConformanceChecking conformanceChecking = new POEMSConformanceCheckingEMSC24Adapter(activityFactory);
            POEMSConformanceCheckingResult result = conformanceChecking.calculateConformance(modelLanguage,
                    logLanguage);
            long endTime = System.currentTimeMillis();
            return new ExperimentResult(bpmnDiagram, result, rgStaticAnalysis, modelLanguage, endTime - startTime);
        } catch (BpmnUnboundedException | BpmnNoOptionToCompleteException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static class ExperimentResult {
        private final StochasticBPMNDiagram bpmnDiagram;
        private final POEMSConformanceCheckingResult ccResult;
        private final StochasticReachabilityGraphStaticAnalysis<BpmnMarking> reachabilityGraphStaticAnalysis;
        private final BpmnStochasticPOTraceLanguage modelLanguage;
        private final long executionTimeMilis;

        private ExperimentResult(StochasticBPMNDiagram bpmnDiagram, POEMSConformanceCheckingResult ccResult, StochasticReachabilityGraphStaticAnalysis<BpmnMarking> reachabilityGraphStaticAnalysis, BpmnStochasticPOTraceLanguage modelLanguage, long executionTimeMilis) {
            this.bpmnDiagram = bpmnDiagram;
            this.ccResult = ccResult;
            this.reachabilityGraphStaticAnalysis = reachabilityGraphStaticAnalysis;
            this.modelLanguage = modelLanguage;
            this.executionTimeMilis = executionTimeMilis;
        }

        @Override
        public String toString() {
            long numSGates = bpmnDiagram.getNodes().stream().filter(n -> n instanceof StochasticGateway).count();
            long numSEdges = bpmnDiagram.getEdges().stream().filter(n -> n instanceof StochasticFlow).count();
            return String.format("%s - #nodes: %d #sgates: %d, #edges: %d, #sedges: %d, %s, languageSize: %d, languageProbability: %s, executionTime: %d",
                    ccResult,
                    bpmnDiagram.getNodes().size(),
                    numSGates,
                    bpmnDiagram.getEdges().size(),
                    numSEdges,
                    reachabilityGraphStaticAnalysis,
                    modelLanguage.size(),
                    modelLanguage.getProbability().getValue().setScale(5, RoundingMode.HALF_EVEN).stripTrailingZeros(),
                    executionTimeMilis);
        }
    }
}
