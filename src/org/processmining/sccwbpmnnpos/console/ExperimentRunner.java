package org.processmining.sccwbpmnnpos.console;

import org.deckfour.xes.model.XLog;
import org.processmining.sccwbpmnnpos.utils.log.XLogReader;
import org.processmining.stochasticbpmn.algorithms.diagram.reader.StochasticBPMNDiagramReader;
import org.processmining.stochasticbpmn.algorithms.reader.ObjectReader;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticBPMNDiagram;

public class ExperimentRunner {
    private final ObjectReader<String, StochasticBPMNDiagram> modelReader;
    private final ObjectReader<String, XLog> logReader;

    public ExperimentRunner() {
        this.modelReader = StochasticBPMNDiagramReader.fromFileName();
        this.logReader = XLogReader.fromFileName();
    }

    public static void main(String[] args) {
        new ExperimentRunner().runExperiments("/home/aleks/Documents/DataSets/ProcessMining/Logs", "/home/aleks/Documents/Learn/Playground/obsidianTest/alkuzman/Research/Concepts/Process Management/Process Mining/Process Models/BPMN/Stochastic/Instances/Logs/");
    }

    private void runExperiments(String logFolder, String modelFolder) {

    }
}
