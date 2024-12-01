package org.processmining.sccwbpmnnpos.console;

import org.processmining.models.graphbased.directed.transitionsystem.ReachabilityGraph;
import org.processmining.plugins.graphviz.dot.Dot;
import org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.stochastic.statespace.StochasticBpmn2POReachabilityGraphConverter;
import org.processmining.sccwbpmnnpos.algorithms.inputs.reachability_graph.stochastic.StochasticReachabilityGraphStaticAnalysis;
import org.processmining.sccwbpmnnpos.algorithms.inputs.reachability_graph.stochastic.StochasticReachabilityGraphStaticAnalyzer;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.BpmnMarking;
import org.processmining.sccwbpmnnpos.models.bpmn.stochastic.execution.node.factory.ExecutableStochasticBpmnNodeFactory;
import org.processmining.stochasticbpmn.algorithms.diagram.reader.StochasticBPMNDiagramReader;
import org.processmining.stochasticbpmn.algorithms.reader.ObjectReader;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticBPMNDiagram;

public class VisualizeReachabilityGraphRunner {
    private final ObjectReader<String, StochasticBPMNDiagram> diagramReader;
    private final StochasticBpmn2POReachabilityGraphConverter sbpmn2Rg;
    private final StochasticReachabilityGraphStaticAnalyzer<BpmnMarking> rgAnalyzer;

    public VisualizeReachabilityGraphRunner() {
        this.diagramReader = StochasticBPMNDiagramReader.fromFileName();
        this.sbpmn2Rg =
                StochasticBpmn2POReachabilityGraphConverter.getInstance(ExecutableStochasticBpmnNodeFactory.getInstance());
        this.rgAnalyzer = StochasticReachabilityGraphStaticAnalyzer.getInstance(BpmnMarking.class);
    }

    public static void main(String[] args) throws Exception {
        VisualizeReachabilityGraphRunner visualizer = new VisualizeReachabilityGraphRunner();
        visualizer.visualize("/home/aleks/Documents/Learn/Playground/obsidianTest/alkuzman/Research/Concepts/Process "
                + "Management/Process Mining/Process Models/BPMN/Stochastic/Instances/ComplexExamples/2 token " +
                "choice/Instance - Stochastic BPMN - 2 token choice - alkuzman.bpmn");
    }

    final void visualize(String bpmnPath) throws Exception {
        StochasticBPMNDiagram diagram = diagramReader.read(bpmnPath);
        ReachabilityGraph rg = sbpmn2Rg.convert(diagram);
        StochasticReachabilityGraphStaticAnalysis<BpmnMarking> rgAnalysisResult = rgAnalyzer.analyze(rg);
        Dot graphViz = rgAnalysisResult.toGraphViz();

        System.out.println(graphViz);
        System.out.println(rgAnalysisResult.getProbabilityToComplete());
    }
}
