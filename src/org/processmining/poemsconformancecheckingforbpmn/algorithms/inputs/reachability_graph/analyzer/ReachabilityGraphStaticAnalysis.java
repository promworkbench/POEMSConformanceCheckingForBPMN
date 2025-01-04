package org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.reachability_graph.analyzer;

import org.processmining.models.graphbased.directed.transitionsystem.ReachabilityGraph;
import org.processmining.plugins.graphviz.dot.Dot;
import org.processmining.poemsconformancecheckingforbpmn.models.execution.Marking;

import java.util.Set;

public interface ReachabilityGraphStaticAnalysis<M extends Marking<?>> {
    ReachabilityGraph getReachabilityGraph();

    Set<M> getDeadLockMarkings();

    Set<M> getMarkingsWithNoOptionToComplete();

    ReachabilityGraph getFixedReachabilityGraph();

    Dot toGraphViz();
}
