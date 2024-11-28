package org.processmining.sccwbpmnnpos.console;

import org.apache.log4j.Appender;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.lf5.LogLevel;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.config.Loggers;
import org.apache.logging.log4j.status.StatusLogger;
import org.deckfour.xes.model.XLog;
import org.processmining.sccwbpmnnpos.algorithms.conformance_checking.poems.POEMSConformanceCheckingEMSC24Adapter;
import org.processmining.sccwbpmnnpos.algorithms.inputs.stochastic_language.StochasticLanguageGenerator;
import org.processmining.sccwbpmnnpos.models.bpmn.conformance.result.POEMSConformanceCheckingResult;
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
        ObjectReader<String, XLog> logReader = XLogReader.fromFileName();
        ActivityFactory activityFactory = ActivityFactory.getInstance();
        StochasticLanguageGenerator languageGenerator = StochasticLanguageGenerator.getInstance(activityFactory);
        ObjectReader<String, StochasticBPMNDiagram> diagramReader = StochasticBPMNDiagramReader.fromFileName();
        POEMSConformanceCheckingEMSC24Adapter POEMSConformanceCheckingEmsc24Adapter = new POEMSConformanceCheckingEMSC24Adapter(activityFactory);

        try {
            LOGGER.error("Reading Log");
            final XLog log = logReader.read
                    ("/home/aleks/Documents/Learn/MasterThesis/SCCwBPMN/Data/Road Trafic Fines/Log/Road_Traffic_Fine_Management_Process.xes");
            LOGGER.error("Reading Stochastic BPMN");
            StochasticBPMNDiagram diagram = diagramReader.read("/home/aleks/Documents/Learn/Playground/obsidianTest/alkuzman/Research/Concepts/Process Management/Process Mining/Process Models/BPMN/Stochastic/Instances/Instance - Stochastic BPMN - rtfm_IMf02_ABE.bpmn");

            LOGGER.error("Generating Log Stochastic Language");
            EventLogStochasticTOTraceLanguage stochasticLogLanguage = languageGenerator.trace(log);
            LOGGER.error("Generating BPMN Stochastic Language");
            BpmnStochasticPOTraceLanguage stochasticModelLanguage = languageGenerator.poTrace(diagram);
            LOGGER.error("Calculating POEMS conformance checking");
            POEMSConformanceCheckingResult poemsConformanceCheckingResult = POEMSConformanceCheckingEmsc24Adapter.calculateConformance(stochasticModelLanguage, stochasticLogLanguage);
            System.out.println(poemsConformanceCheckingResult);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
