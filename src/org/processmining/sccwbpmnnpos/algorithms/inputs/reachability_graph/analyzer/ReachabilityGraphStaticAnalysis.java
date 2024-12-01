package org.processmining.sccwbpmnnpos.algorithms.inputs.reachability_graph.analyzer;

import org.processmining.models.graphbased.directed.transitionsystem.ReachabilityGraph;
import org.processmining.plugins.graphviz.dot.Dot;
import org.processmining.sccwbpmnnpos.models.execution.Marking;
import org.processmining.stochasticbpmn.models.stochastic.Probability;

import java.util.Set;

public interface ReachabilityGraphStaticAnalysis<M extends Marking<?>> {
    ReachabilityGraph getReachabilityGraph();

    Set<M> getDeadLockMarkings();

    Set<M> getMarkingsWithNoOptionToComplete();

    ReachabilityGraph getFixedReachabilityGraph();

    Dot toGraphViz();
}
