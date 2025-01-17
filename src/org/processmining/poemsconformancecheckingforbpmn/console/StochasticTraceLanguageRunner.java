package org.processmining.poemsconformancecheckingforbpmn.console;

import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.stochastic_language.StochasticLanguageGenerator;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.utils.stochastics.sampling.stopping.SampleSizeStoppingCriterion;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.utils.stochastics.sampling.strategy.transition.TansitionSamplingStrategyType;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.trace.BpmnPartiallyOrderedTrace;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.stochastic.language.trace.BpmnStochasticPOTraceLanguage;
import org.processmining.poemsconformancecheckingforbpmn.models.stochastic.language.StochasticLanguageEntry;
import org.processmining.poemsconformancecheckingforbpmn.models.utils.activity.Activity;
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
                StochasticLanguageGenerator.getInstance(TansitionSamplingStrategyType.MOST_PROBABLE, prob -> new SampleSizeStoppingCriterion(languageSize));
    }

    public static void main(String[] args) throws Exception {
        StochasticTraceLanguageRunner visualizer = new StochasticTraceLanguageRunner(10);
        visualizer.visualize("/home/aleks/Documents/Learn/Playground/obsidianTest/alkuzman/Research/Concepts/Process Management/Process Mining/Process Models/BPMN/Instances/Logs/Handling of Compensation Requests/Hand Made/Instance - BPMN - Handling of Compensation Requests.bpmn");
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
