package org.processmining.poemsconformancecheckingforbpmn.console;

import org.processmining.models.graphbased.directed.transitionsystem.ReachabilityGraph;
import org.processmining.plugins.graphviz.dot.Dot;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.bpmn.stochastic.statespace.StochasticBpmn2POReachabilityGraphConverter;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.reachability_graph.stochastic.analyzer.StochasticReachabilityGraphStaticAnalysis;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.reachability_graph.stochastic.analyzer.StochasticReachabilityGraphStaticAnalyzer;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.marking.BpmnMarking;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.stochastic.execution.node.factory.ExecutableStochasticBpmnNodeFactory;
import org.processmining.stochasticbpmn.algorithms.diagram.reader.StochasticBPMNDiagramFromSPNReader;
import org.processmining.stochasticbpmn.algorithms.diagram.reader.StochasticBPMNDiagramReader;
import org.processmining.stochasticbpmn.algorithms.reader.ObjectReader;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticBPMNDiagram;

public class VisualizeStochasticBPMNReachabilityGraphRunner {
    private final ObjectReader<String, StochasticBPMNDiagram> diagramReader;
    private final ObjectReader<String, StochasticBPMNDiagram> spnDiagramReader;
    private final StochasticBpmn2POReachabilityGraphConverter sbpmn2Rg;
    private final StochasticReachabilityGraphStaticAnalyzer<BpmnMarking> rgAnalyzer;

    public VisualizeStochasticBPMNReachabilityGraphRunner() {
        this.diagramReader = StochasticBPMNDiagramReader.fromFileName();
        this.sbpmn2Rg =
                StochasticBpmn2POReachabilityGraphConverter.getInstance(ExecutableStochasticBpmnNodeFactory.getInstance());
        this.rgAnalyzer = StochasticReachabilityGraphStaticAnalyzer.getInstance(BpmnMarking.class);
        this.spnDiagramReader = StochasticBPMNDiagramFromSPNReader.fromFileName();
    }

    public static void main(String[] args) throws Exception {
        VisualizeStochasticBPMNReachabilityGraphRunner visualizer = new VisualizeStochasticBPMNReachabilityGraphRunner();
        visualizer.visualize("/home/aleks/Documents/DataResources/ProcessMining/BPMN/Logs/Handling of Compensation Requests/Hand Made/Instance - BPMN - Handling of Compensation Requests with Livelock.bpmn");
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
