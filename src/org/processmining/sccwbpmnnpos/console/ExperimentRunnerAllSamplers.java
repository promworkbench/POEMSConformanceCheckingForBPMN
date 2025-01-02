package org.processmining.sccwbpmnnpos.console;

import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import com.opencsv.CSVWriter;
import org.apache.commons.lang3.ArrayUtils;
import org.deckfour.xes.classification.XEventNameClassifier;
import org.deckfour.xes.model.XLog;
import org.processmining.models.graphbased.directed.transitionsystem.ReachabilityGraph;
import org.processmining.sccwbpmnnpos.algorithms.conformance_checking.poems.POEMSConformanceChecking;
import org.processmining.sccwbpmnnpos.algorithms.conformance_checking.poems.POEMSConformanceCheckingEMSC24Adapter;
import org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.statespace.BpmnNoOptionToCompleteException;
import org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.statespace.BpmnUnboundedException;
import org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.stochastic.statespace.StochasticBpmn2POReachabilityGraphConverter;
import org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.stochastic.statespace.language.trace.StochasticBpmnPORG2StochasticTraceLanguageConverter;
import org.processmining.sccwbpmnnpos.algorithms.inputs.log.simplifier.XLogSimplifier;
import org.processmining.sccwbpmnnpos.algorithms.inputs.log.stohastic_language.simplified.SimplifiedLog2StochasticLanguageConverter;
import org.processmining.sccwbpmnnpos.algorithms.inputs.reachability_graph.stochastic.analyzer.StochasticReachabilityGraphStaticAnalysis;
import org.processmining.sccwbpmnnpos.algorithms.inputs.reachability_graph.stochastic.analyzer.StochasticReachabilityGraphStaticAnalyzer;
import org.processmining.sccwbpmnnpos.algorithms.utils.stochastics.sampling.stopping.SampleProbabilityMassStoppingCriterion;
import org.processmining.sccwbpmnnpos.algorithms.utils.stochastics.sampling.stopping.SamplingStoppingCriterion;
import org.processmining.sccwbpmnnpos.algorithms.utils.stochastics.sampling.strategy.graph.TansitionSamplingStrategyType;
import org.processmining.sccwbpmnnpos.models.bpmn.conformance.result.POEMSConformanceCheckingResult;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.BpmnMarking;
import org.processmining.sccwbpmnnpos.models.bpmn.stochastic.language.trace.BpmnStochasticPOTraceLanguage;
import org.processmining.sccwbpmnnpos.models.log.SimplifiedEventLog;
import org.processmining.sccwbpmnnpos.models.log.stochastic.language.EventLogStochasticTOTraceLanguage;
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
import java.io.FileWriter;
import java.io.IOException;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class ExperimentRunnerAllSamplers {
    private static final Logger logger = LoggerFactory.getLogger(ExampleRunner.class);
    private final ObjectReader<File, XLog> logReader;
    private final SimplifiedLog2StochasticLanguageConverter slog2slConverter;
    private final ObjectReader<File, StochasticBPMNDiagram> modelReader;
    private final ObjectReader<File, StochasticBPMNDiagram> spnReader;
    private final StochasticBpmn2POReachabilityGraphConverter sbpmn2Rg;
    private final StochasticReachabilityGraphStaticAnalyzer<BpmnMarking> rgAnalyzer;

    public ExperimentRunnerAllSamplers() {
        this.logReader = XLogReader.fromFile();
        this.slog2slConverter = SimplifiedLog2StochasticLanguageConverter.getInstance();
        this.modelReader = StochasticBPMNDiagramReader.fromFile();
        this.sbpmn2Rg = StochasticBpmn2POReachabilityGraphConverter.getInstance();
        this.rgAnalyzer = StochasticReachabilityGraphStaticAnalyzer.getInstance(BpmnMarking.class);
        this.spnReader = StochasticBPMNDiagramFromSPNReader.fromFile();
    }

    public static void main(String[] args) {
        File logsFolder = null;
        File modelsFolder = null;
        File resultsFolder = null;
//        File logsFolder = new File("C:\\Users\\peeva\\My\\evaluation\\stochastic\\conformance_checking\\logs");
//        File modelsFolder = new File("C:\\Users\\peeva\\My\\evaluation\\stochastic\\conformance_checking\\models");
//        File resultsFolder = new File("C:\\Users\\peeva\\My\\evaluation\\stochastic\\conformance_checking\\results");
        if (args.length == 1) {
            System.out.println(args[0]);
            logsFolder = new File(args[0] + File.separator + "logs");
            modelsFolder = new File(args[0] + File.separator + "models");
            resultsFolder = new File(args[0] + File.separator + "results");
        } else if (args.length == 3) {
            logsFolder = new File(args[0]);
            modelsFolder = new File(args[1]);
            resultsFolder = new File(args[2]);
        }
        new ExperimentRunnerAllSamplers().runExperiments(
                logsFolder,
                modelsFolder,
                resultsFolder
        );
    }

    private void runExperiments(
            File logsFolder,
            File modelsFolder,
            File resultsFolder
    ) {
        // logging
        ImmutableTable.Builder<String, String, Integer> logInfoTB = new ImmutableTable.Builder<>();
        ImmutableTable.Builder<String, String, String> modelInfoTB = new ImmutableTable.Builder<>();
        ImmutableTable.Builder<String, String, String> resultsInfoTB = new ImmutableTable.Builder<>();
        File resultFolderConcreteRun = setupLogging(resultsFolder);

        Map<String, File> logFiles = getLogs(logsFolder);
        Map<String, Map<String, File>> modelLogFiles = getModels(modelsFolder);
        Set<String> uniqueModelVariants =
                modelLogFiles.values().stream().flatMap(m -> m.keySet().stream()).collect(Collectors.toSet());
        for (Map.Entry<String, File> logFile : logFiles.entrySet()) {
            Map<String, File> modelFiles = modelLogFiles.get(logFile.getKey());
            if (Objects.isNull(modelFiles) || modelFiles.isEmpty()) {
                continue;
            }
            try {
                XLog log = logReader.read(logFile.getValue());
                ActivityFactory activityFactory = ActivityFactory.getInstance();
                SimplifiedEventLog slog = XLogSimplifier.getInstance(
                        new XEventNameClassifier(),
                        activityFactory
                ).simplify(log);
                EventLogStochasticTOTraceLanguage logLanguage = slog2slConverter.convert(slog);

                recordLogInfo(
                        logFile,
                        slog,
                        logLanguage,
                        logInfoTB
                );

                for (Map.Entry<String, File> modelFileVariant : modelFiles.entrySet()) {
                    String[] modelFileParts = modelFileVariant.getValue().getName().split("\\.");
                    String extension = modelFileParts[modelFileParts.length - 1];
                    StochasticBPMNDiagram model;
                    if (Objects.equals(
                            extension,
                            "bpmn"
                    )) {
                        model = modelReader.read(modelFileVariant.getValue());
                    } else {
                        model = spnReader.read(modelFileVariant.getValue());
                    }
//                    model = modelReader.read(modelFileVariant.getValue());
                    System.out.println("Run: " + modelFileVariant.getValue().getParentFile().getName());
                    try {
                        for (TansitionSamplingStrategyType samplingType : TansitionSamplingStrategyType.values()) {
                            ExperimentResult result =
                                    runExperiments(
                                            logLanguage,
                                            model,
                                            activityFactory,
                                            samplingType
                                    );
                            System.out.printf(
                                    "%s: %s\n",
                                    modelFileVariant.getKey(),
                                    result
                            );
                            recordModelInfo(
                                    logFile.getKey() + "_" + modelFileVariant.getKey(),
                                    modelInfoTB,
                                    result
                            );
                            recordResults(
                                    logFile.getKey() + "_" + modelFileVariant.getKey(),
                                    resultsInfoTB,
                                    result
                            );
                            writeResults(
                                    resultFolderConcreteRun,
                                    logInfoTB,
                                    modelInfoTB,
                                    resultsInfoTB
                            );
                        }
                    } catch (Exception e) {
                        logger.error(
                                String.format(
                                        "Failure to execute %s",
                                        modelFileVariant.getValue().getParentFile().getName()
                                ),
                                e
                        );
                    }
                }
            } catch (Exception e) {
                logger.error(
                        String.format(
                                "Failure to read log from %s",
                                logFile.getValue().getPath()
                        ),
                        e
                );
            }
        }
        writeResults(
                resultFolderConcreteRun,
                logInfoTB,
                modelInfoTB,
                resultsInfoTB
        );
    }

    private static File setupLogging(File resultFolder) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HHmmss");
        File runResultFolder = new File(
                resultFolder,
                LocalDateTime.now().format(formatter)
        );
        runResultFolder.mkdirs();

        return runResultFolder;
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
                    logs.put(
                            logFolder.getName(),
                            logFile
                    );
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
            Map<String, File> modelVariants = models.computeIfAbsent(
                    logFolder.getName(),
                    k -> new HashMap<>()
            );
            for (File modelVariantFolder : modelVariantFolders) {
                File[] modelFiles = modelVariantFolder.listFiles();
                if (Objects.isNull(modelFiles)) {
                    continue;
                }
                for (File modelFile : modelFiles) {
                    String[] modelFileParts = modelFile.getName().split("\\.");
                    String extension = modelFileParts[modelFileParts.length - 1];
                    if (!(Objects.equals(
                            extension,
                            "bpmn"
                    ) || Objects.equals(
                            extension,
                            "pnml"
                    ))) {
                        continue;
                    }
                    modelVariants.put(
                            modelVariantFolder.getName(),
                            modelFile
                    );
                }
            }
        }

        return models;
    }

    private static void recordLogInfo(
            Map.Entry<String, File> logFile,
            SimplifiedEventLog slog,
            EventLogStochasticTOTraceLanguage logLanguage,
            ImmutableTable.Builder<String, String, Integer> logInfoTB
    ) {
        System.out.printf(
                "%s - traces: %d, variants: %d\n",
                logFile.getKey(),
                slog.size(),
                logLanguage.size()
        );
        logInfoTB.put(
                logFile.getKey(),
                "Traces",
                slog.size()
        );
        logInfoTB.put(
                logFile.getKey(),
                "Variants",
                logLanguage.size()
        );
    }

    private ExperimentResult runExperiments(
            EventLogStochasticTOTraceLanguage logLanguage,
            StochasticBPMNDiagram bpmnDiagram,
            ActivityFactory activityFactory,
            TansitionSamplingStrategyType samplingType
    ) {
        try {
            long startTime = System.currentTimeMillis();
            ReachabilityGraph rg = sbpmn2Rg.convert(bpmnDiagram);
            StochasticReachabilityGraphStaticAnalysis<BpmnMarking> rgStaticAnalysis = rgAnalyzer.analyze(rg);
            ReachabilityGraph fixedRg = rgStaticAnalysis.getFixedReachabilityGraph();
            Probability requiredProbability =
                    rgStaticAnalysis.getProbabilityToComplete().subtract(Probability.of(0.000001));

            SamplingStoppingCriterion stopper;
            ExperimentConfig experimentConfig;
            if (samplingType.equals(TansitionSamplingStrategyType.MOST_PROBABLE)) {
                stopper = new SampleProbabilityMassStoppingCriterion(requiredProbability);
                experimentConfig = new ExperimentConfig(
                        0.000001,
                        samplingType,
                        "Probability",
                        requiredProbability.doubleValue()
                );
            } else {
                stopper = SamplingStoppingCriterion.getInstance();
                experimentConfig = new ExperimentConfig(
                        0.000001,
                        samplingType,
                        "Size",
                        1000
                );
            }
            StochasticBpmnPORG2StochasticTraceLanguageConverter languageGenerator =
                    StochasticBpmnPORG2StochasticTraceLanguageConverter.getInstance(
                            activityFactory,
                            samplingType,
                            stopper,
                            1000
                    );
            BpmnStochasticPOTraceLanguage modelLanguage = languageGenerator.convert(fixedRg);

            POEMSConformanceChecking conformanceChecking = new POEMSConformanceCheckingEMSC24Adapter(activityFactory);
            POEMSConformanceCheckingResult result = conformanceChecking.calculateConformance(
                    modelLanguage,
                    logLanguage
            );
            long endTime = System.currentTimeMillis();
            return new ExperimentResult(
                    bpmnDiagram,
                    result,
                    rgStaticAnalysis,
                    modelLanguage,
                    endTime - startTime,
                    experimentConfig
            );
        } catch (BpmnUnboundedException | BpmnNoOptionToCompleteException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void recordModelInfo(
            String key,
            ImmutableTable.Builder<String, String, String> modelInfoTB,
            ExperimentResult result
    ) {
        modelInfoTB.put(
                key,
                "BPMN Nodes Count",
                String.valueOf(result.bpmnDiagram.getNodes().size())
        );
        modelInfoTB.put(
                key,
                "BPMN Edge Count",
                String.valueOf(result.bpmnDiagram.getEdges().size())
        );
        long numSGates = result.bpmnDiagram.getNodes().stream()
                .filter(n -> n instanceof StochasticGateway).count();
        long numSEdges = result.bpmnDiagram.getEdges().stream()
                .filter(n -> n instanceof StochasticFlow).count();
        modelInfoTB.put(
                key,
                "Stochastic Gates Count",
                String.valueOf(numSGates)
        );
        modelInfoTB.put(
                key,
                "Stochastic Edges Count",
                String.valueOf(numSEdges)
        );
        modelInfoTB.put(
                key,
                "Maximum Probability",
                result.reachabilityGraphStaticAnalysis.getProbabilityToComplete().toString()
        );
        modelInfoTB.put(
                key,
                "RG Nodes Count",
                String.valueOf(result.reachabilityGraphStaticAnalysis.getReachabilityGraph().getNodes().size())
        );
        modelInfoTB.put(
                key,
                "RG Edges Count",
                String.valueOf(result.reachabilityGraphStaticAnalysis.getReachabilityGraph().getEdges().size())
        );
        modelInfoTB.put(
                key,
                "RG No Option To Complete Markings",
                String.valueOf(result.reachabilityGraphStaticAnalysis.getMarkingsWithNoOptionToComplete().size())
        );
        modelInfoTB.put(
                key,
                "RG Dead Lock Markings",
                String.valueOf(result.reachabilityGraphStaticAnalysis.getDeadLockMarkings().size())
        );
    }

    private static void recordResults(
            String key,
            ImmutableTable.Builder<String, String, String> resultsInfoTB,
            ExperimentResult result
    ) {
        resultsInfoTB.put(
                key,
                "CC Result Lower Bound",
                result.ccResult.getConformanceLowerBound().toString()
        );
        resultsInfoTB.put(
                key,
                "CC Result Upper Bound",
                result.ccResult.getConformanceUpperBound().toString()
        );
        resultsInfoTB.put(
                key,
                "Model Language Size",
                String.valueOf(result.modelLanguage.size())
        );
        resultsInfoTB.put(
                key,
                "Model Language Probability",
                result.modelLanguage.getProbability().getValue()
                        .setScale(
                                5,
                                RoundingMode.HALF_EVEN
                        ).stripTrailingZeros().toString()
        );
        resultsInfoTB.put(
                key,
                "Execution Time",
                String.valueOf(result.executionTimeMilis)
        );
        resultsInfoTB.put(
                key,
                "Sampling Strategy",
                result.experimentConfig.graphSamplingType.name()
        );
        resultsInfoTB.put(
                key,
                "Stopping Criterion",
                result.experimentConfig.stopperType
        );
        resultsInfoTB.put(
                key,
                "Stopping Value",
                String.valueOf(result.experimentConfig.stoppingValue)
        );
    }

    private static void writeResults(
            File resultFolder,
            ImmutableTable.Builder<String, String, Integer> logInfoTB,
            ImmutableTable.Builder<String, String, String> modelInfoTB,
            ImmutableTable.Builder<String, String, String> resultsTB
    ) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HHmmss");
//        File runResultFolder = new File(resultFolder, LocalDateTime.now().format(formatter));
//        runResultFolder.mkdirs();
        // logs
        writeTableInCsv(
                resultFolder.getPath() + File.separator + "logs.csv",
                logInfoTB
        );
        writeTableInCsv(
                resultFolder.getPath() + File.separator + "models.csv",
                modelInfoTB
        );
        writeTableInCsv(
                resultFolder.getPath() + File.separator + "results.csv",
                resultsTB
        );
    }

    private static void writeTableInCsv(
            String resultsPath,
            ImmutableTable.Builder<String, String, ?> tablebuilder
    ) {
        Table<String, String, ?> table = tablebuilder.build();
        List<String> columns = new ArrayList<>(table.columnKeySet());
        try (CSVWriter csvWriter = new CSVWriter(new FileWriter(resultsPath))) {
            csvWriter.writeNext(
                    ArrayUtils.add(
                            columns.toArray(new String[0]),
                            0,
                            "Key"
                    ),
                    true
            );
            for (String rowKey : table.rowMap().keySet()) {
                Map<String, ?> row = table.rowMap().get(rowKey);
                String[] nextLine =
                        ArrayUtils.add(
                                columns.stream().map(row::get).map(Object::toString).toArray(String[]::new),
                                0
                                ,
                                rowKey
                        );
                csvWriter.writeNext(
                        nextLine,
                        true
                );
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static class ExperimentResult {
        private final StochasticBPMNDiagram bpmnDiagram;
        private final POEMSConformanceCheckingResult ccResult;
        private final StochasticReachabilityGraphStaticAnalysis<BpmnMarking> reachabilityGraphStaticAnalysis;
        private final BpmnStochasticPOTraceLanguage modelLanguage;
        private final long executionTimeMilis;
        private final ExperimentConfig experimentConfig;

        private ExperimentResult(
                StochasticBPMNDiagram bpmnDiagram,
                POEMSConformanceCheckingResult ccResult,
                StochasticReachabilityGraphStaticAnalysis<BpmnMarking> reachabilityGraphStaticAnalysis,
                BpmnStochasticPOTraceLanguage modelLanguage,
                long executionTimeMilis,
                ExperimentConfig experimentConfig
        ) {
            this.bpmnDiagram = bpmnDiagram;
            this.ccResult = ccResult;
            this.reachabilityGraphStaticAnalysis = reachabilityGraphStaticAnalysis;
            this.modelLanguage = modelLanguage;
            this.executionTimeMilis = executionTimeMilis;
            this.experimentConfig = experimentConfig;
        }

        @Override
        public String toString() {
            long numSGates = bpmnDiagram.getNodes().stream().filter(n -> n instanceof StochasticGateway).count();
            long numSEdges = bpmnDiagram.getEdges().stream().filter(n -> n instanceof StochasticFlow).count();
            return String.format(
                    "%s - #nodes: %d #sgates: %d, #edges: %d, #sedges: %d, %s, languageSize: %d, languageProbability:" +
                            " %s, executionTime: %d",
                    ccResult,
                    bpmnDiagram.getNodes().size(),
                    numSGates,
                    bpmnDiagram.getEdges().size(),
                    numSEdges,
                    reachabilityGraphStaticAnalysis,
                    modelLanguage.size(),
                    modelLanguage.getProbability().getValue().setScale(
                            5,
                            RoundingMode.HALF_EVEN
                    ).stripTrailingZeros(),
                    executionTimeMilis
            );
        }
    }

    private static class ExperimentConfig {
        private final double roundingPrecision;
        private final TansitionSamplingStrategyType graphSamplingType;
        private final String stopperType;
        private final double stoppingValue;

        public ExperimentConfig(
                double roundingPrecision,
                TansitionSamplingStrategyType graphSamplingType,
                String stopperType,
                double stoppingValue
        ) {
            this.roundingPrecision = roundingPrecision;
            this.graphSamplingType = graphSamplingType;
            this.stopperType = stopperType;
            this.stoppingValue = stoppingValue;
        }
    }
}
