package org.processmining.sccwbpmnnpos.console;

import org.processmining.sccwbpmnnpos.algorithms.inputs.stochastic_language.StochasticLanguageGenerator;
import org.processmining.sccwbpmnnpos.algorithms.inputs.stochastic_language.stopping.NumElementsStochasticLanguageGeneratorStopper;
import org.processmining.sccwbpmnnpos.algorithms.utils.stochastics.sampling.strategy.graph.StochasticGraphPathSamplingStrategy;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.trace.BpmnPartiallyOrderedTrace;
import org.processmining.sccwbpmnnpos.models.bpmn.stochastic.language.trace.BpmnStochasticPOTraceLanguage;
import org.processmining.sccwbpmnnpos.models.stochastic.language.StochasticLanguageEntry;
import org.processmining.sccwbpmnnpos.models.utils.activity.Activity;
import org.processmining.stochasticbpmn.algorithms.diagram.reader.StochasticBPMNDiagramReader;
import org.processmining.stochasticbpmn.algorithms.reader.ObjectReader;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticBPMNDiagram;

import java.util.Objects;

public class StochasticTraceLanguageRunner {
    private final ObjectReader<String, StochasticBPMNDiagram> diagramReader;
    private final StochasticLanguageGenerator languageGenerator;
    private final int languageSize;

    public StochasticTraceLanguageRunner(int languageSize) {
        this.languageSize = languageSize;
        this.diagramReader = StochasticBPMNDiagramReader.fromFileName();
        this.languageGenerator =
                StochasticLanguageGenerator.getInstance(StochasticGraphPathSamplingStrategy.GraphSamplingType.MOST_PROBABLE, new NumElementsStochasticLanguageGeneratorStopper(languageSize));
    }

    public static void main(String[] args) throws Exception {
        StochasticTraceLanguageRunner visualizer = new StochasticTraceLanguageRunner(10000);
        visualizer.visualize("/home/aleks/Documents/Learn/Playground/obsidianTest/alkuzman/Research/Concepts/Process "
                + "Management/Process Mining/Process Models/BPMN/Stochastic/Instances/ComplexExamples/2 token " +
                "choice/Instance - Stochastic BPMN - 2 token choice - alkuzman.bpmn");
    }

    final void visualize(String bpmnPath) throws Exception {
        StochasticBPMNDiagram diagram = diagramReader.read(bpmnPath);
        BpmnStochasticPOTraceLanguage stochasticLanguage = languageGenerator.poTrace(diagram);
        StochasticLanguageEntry<Activity, BpmnPartiallyOrderedTrace> longestTrace = null;
        for (StochasticLanguageEntry<Activity, BpmnPartiallyOrderedTrace> trace : stochasticLanguage) {
            if (Objects.isNull(longestTrace) || longestTrace.getElement().size() < trace.getElement().size()) {
                longestTrace = trace;
            }
        }
        System.out.println(stochasticLanguage.size());
//        if (stochasticLanguage.size() > 0) {
//            System.out.println(longestTrace.getElement());
//            System.out.println(longestTrace.getProbability());
//        }
        System.out.println(stochasticLanguage.toGraphViz().toString());
    }
}
