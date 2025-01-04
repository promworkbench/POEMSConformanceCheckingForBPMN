package org.processmining.poemsconformancecheckingforbpmn.console;

import org.processmining.models.graphbased.directed.bpmn.BPMNDiagram;
import org.processmining.models.graphbased.directed.transitionsystem.ReachabilityGraph;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.bpmn.statespace.Bpmn2POReachabilityGraphConverter;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.reachability_graph.analyzer.ReachabilityGraphStaticAnalysis;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.reachability_graph.analyzer.ReachabilityGraphStaticAnalyzer;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.marking.BpmnMarking;
import org.processmining.stochasticbpmn.algorithms.diagram.reader.BpmnDiagramReader;
import org.processmining.stochasticbpmn.algorithms.reader.ObjectReader;

import java.io.File;

public class VisualizeBPMNReachabilityGraphRunner {
    private final ObjectReader<File, BPMNDiagram> diagramReader;
    private final Bpmn2POReachabilityGraphConverter bpmn2Rg;
    private final ReachabilityGraphStaticAnalyzer<BpmnMarking> rgAnalyzer;

    public VisualizeBPMNReachabilityGraphRunner() {
        this.diagramReader = BpmnDiagramReader.fromFile();
        this.bpmn2Rg = Bpmn2POReachabilityGraphConverter.getInstance();
        this.rgAnalyzer = ReachabilityGraphStaticAnalyzer.getInstance(BpmnMarking.class);
    }

    public static void main(String[] args) throws Exception {
        VisualizeBPMNReachabilityGraphRunner visualizer = new VisualizeBPMNReachabilityGraphRunner();
        String bpmnPath = "/home/aleks/Documents/Learn/Playground/obsidianTest/alkuzman/Research/Concepts/Process Management/Process Mining/Process Models/BPMN/Instances/Examples/No option to complete/Livelock and Deadlock/Instance - BPMN - Livelock and Deadlock.bpmn";

        File bpmnFile = new File(bpmnPath);
        visualizer.visualize(bpmnFile);
    }

    final void visualize(File bpmnFile) throws Exception {
        BPMNDiagram diagram = diagramReader.read(bpmnFile);
        ReachabilityGraph rg = bpmn2Rg.convert(diagram);
        ReachabilityGraphStaticAnalysis<BpmnMarking> rgAnalysis = rgAnalyzer.analyze(rg);

        String[] nameParts = bpmnFile.getName().split("\\.")[0].split(" - ");
        String namePart = nameParts[nameParts.length - 1];
        String resultFileName = "Instance - BPMN Reachability Graph - " + namePart + ".dot";
        File resultFile = new File(bpmnFile.getParentFile(), resultFileName);
        rgAnalysis.toGraphViz().exportToFile(resultFile);
        System.out.println(rgAnalysis.toGraphViz().toString());
    }
}
