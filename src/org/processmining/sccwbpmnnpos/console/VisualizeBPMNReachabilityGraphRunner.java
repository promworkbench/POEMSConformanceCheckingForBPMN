package org.processmining.sccwbpmnnpos.console;

import org.processmining.models.graphbased.directed.bpmn.BPMNDiagram;
import org.processmining.models.graphbased.directed.transitionsystem.ReachabilityGraph;
import org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.statespace.Bpmn2POReachabilityGraphConverter;
import org.processmining.sccwbpmnnpos.algorithms.inputs.reachability_graph.ReachabilityGraphUtils;
import org.processmining.sccwbpmnnpos.algorithms.inputs.reachability_graph.analyzer.ReachabilityGraphStaticAnalysis;
import org.processmining.sccwbpmnnpos.algorithms.inputs.reachability_graph.analyzer.ReachabilityGraphStaticAnalyzer;
import org.processmining.sccwbpmnnpos.algorithms.inputs.reachability_graph.stochastic.analyzer.StochasticReachabilityGraphStaticAnalyzer;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.BpmnMarking;
import org.processmining.stochasticbpmn.algorithms.diagram.reader.BpmnDiagramReader;
import org.processmining.stochasticbpmn.algorithms.reader.ObjectReader;

public class VisualizeBPMNReachabilityGraphRunner {
    private final ObjectReader<String, BPMNDiagram> diagramReader;
    private final Bpmn2POReachabilityGraphConverter bpmn2Rg;
    private final ReachabilityGraphStaticAnalyzer<BpmnMarking> rgAnalyzer;

    public VisualizeBPMNReachabilityGraphRunner() {
        this.diagramReader = BpmnDiagramReader.fromFileName();
        this.bpmn2Rg = Bpmn2POReachabilityGraphConverter.getInstance();
        this.rgAnalyzer = ReachabilityGraphStaticAnalyzer.getInstance(BpmnMarking.class);
    }

    public static void main(String[] args) throws Exception {
        VisualizeBPMNReachabilityGraphRunner visualizer = new VisualizeBPMNReachabilityGraphRunner();
        visualizer.visualize("/home/aleks/Documents/Learn/Playground/obsidianTest/alkuzman/Research/Concepts/Process " +
                "Management/Process Mining/Process Models/BPMN/Instances/Examples/No option to complete/Live Lock" +
                "/model_1.bpmn");
    }

    final void visualize(String bpmnPath) throws Exception {
        BPMNDiagram diagram = diagramReader.read(bpmnPath);
        ReachabilityGraph rg = bpmn2Rg.convert(diagram);
        ReachabilityGraphStaticAnalysis<BpmnMarking> rgAnalysis = rgAnalyzer.analyze(rg);

        System.out.println(rgAnalysis.toGraphViz().toString());
    }
}
