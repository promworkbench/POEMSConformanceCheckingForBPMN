package org.processmining.sccwbpmnnpos.console;

import org.processmining.stochasticbpmn.algorithms.diagram.builder.StochasticBPMNDiagramBuilderImpl;
import org.processmining.stochasticbpmn.algorithms.diagram.reader.StochasticBPMNDiagramReader;
import org.processmining.stochasticbpmn.algorithms.reader.StochasticBPMNInputStreamReader;

public class ExperimentRunner {
    public static void main(String[] args) {
        StochasticBPMNDiagramReader reader = new StochasticBPMNDiagramReader(new StochasticBPMNInputStreamReader(), new StochasticBPMNDiagramBuilderImpl());
    }
}
