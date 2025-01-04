package org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.reachability_graph.analyzer;

import org.processmining.models.graphbased.directed.transitionsystem.ReachabilityGraph;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.reachability_graph.ReachabilityGraphUtils;
import org.processmining.poemsconformancecheckingforbpmn.models.execution.Marking;

import java.util.*;

public class ReachabilityGraphStaticAnalyzerImpl<M extends Marking<?>> implements ReachabilityGraphStaticAnalyzer<M> {
    private final Class<M> clazz;

    public ReachabilityGraphStaticAnalyzerImpl(Class<M> clazz) {
        this.clazz = clazz;
    }

    @Override
    public ReachabilityGraphStaticAnalysis<M> analyze(ReachabilityGraph rg) {
        M initialState = ReachabilityGraphUtils.getInitialMarking(rg, clazz);
        Set<M> deadlockMarkings = ReachabilityGraphUtils.getDeadlockMarkings(rg, clazz);
        Set<M> markingsWithNoOptionToComplete = ReachabilityGraphUtils.getMarkingsWithNoOptionToComplete(rg, clazz);

        return new ReachabilityGraphStaticAnalysisDTO<>(rg, initialState, deadlockMarkings,
                markingsWithNoOptionToComplete, clazz);
    }
}
