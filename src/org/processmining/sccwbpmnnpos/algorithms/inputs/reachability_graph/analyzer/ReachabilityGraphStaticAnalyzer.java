package org.processmining.sccwbpmnnpos.algorithms.inputs.reachability_graph.analyzer;

import org.processmining.models.graphbased.directed.transitionsystem.ReachabilityGraph;
import org.processmining.sccwbpmnnpos.algorithms.inputs.reachability_graph.stochastic.analyzer.StochasticReachabilityGraphStaticAnalysis;
import org.processmining.sccwbpmnnpos.models.execution.Marking;

public interface ReachabilityGraphStaticAnalyzer<M extends Marking<?>> {
    static <M extends Marking<?>> ReachabilityGraphStaticAnalyzer<M> getInstance(Class<M> clazz) {
        return new ReachabilityGraphStaticAnalyzerImpl<>(clazz);
    }

    ReachabilityGraphStaticAnalysis<M> analyze(ReachabilityGraph reachabilityGraph);
}
