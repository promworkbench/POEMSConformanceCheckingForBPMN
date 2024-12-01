package org.processmining.sccwbpmnnpos.algorithms.inputs.reachability_graph.stochastic.analyzer;

import org.processmining.models.graphbased.directed.transitionsystem.ReachabilityGraph;
import org.processmining.sccwbpmnnpos.models.execution.Marking;

public interface StochasticReachabilityGraphStaticAnalyzer<M extends Marking<?>> {
    static <M extends Marking<?>> StochasticReachabilityGraphStaticAnalyzer<M> getInstance(Class<M> clazz) {
        return new StochasticReachabilityGraphStaticAnalyzerImpl<>(clazz);
    }

    StochasticReachabilityGraphStaticAnalysis<M> analyze(ReachabilityGraph reachabilityGraph);
}
