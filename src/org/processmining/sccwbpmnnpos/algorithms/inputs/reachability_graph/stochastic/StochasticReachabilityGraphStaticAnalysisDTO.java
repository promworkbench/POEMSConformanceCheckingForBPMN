package org.processmining.sccwbpmnnpos.algorithms.inputs.reachability_graph.stochastic;

import org.processmining.models.graphbased.directed.transitionsystem.ReachabilityGraph;
import org.processmining.models.graphbased.directed.transitionsystem.State;
import org.processmining.models.graphbased.directed.transitionsystem.Transition;
import org.processmining.sccwbpmnnpos.models.execution.Marking;
import org.processmining.stochasticbpmn.models.stochastic.Probability;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class StochasticReachabilityGraphStaticAnalysisDTO<M extends Marking<?>> implements StochasticReachabilityGraphStaticAnalysis<M> {
    private final ReachabilityGraph reachabilityGraph;
    private final Set<M> deadLockMarkings;
    private final Set<M> markingsWithoutOptionToComplete;
    private final Map<M, Probability> markingProbabilityMapping;
    private final M initialMarking;

    public StochasticReachabilityGraphStaticAnalysisDTO(ReachabilityGraph reachabilityGraph, M initialMarking, Set<M> deadLockMarkings,
                                                        Set<M> markingsWithoutOptionToComplete,
                                                        Map<M, Probability> markingProbabilityMapping) {
        this.reachabilityGraph = reachabilityGraph;
        this.deadLockMarkings = deadLockMarkings;
        this.markingsWithoutOptionToComplete = markingsWithoutOptionToComplete;
        this.markingProbabilityMapping = markingProbabilityMapping;
        this.initialMarking = initialMarking;
    }

    @Override
    public ReachabilityGraph getReachabilityGraph() {
        return reachabilityGraph;
    }

    @Override
    public Set<M> getDeadLockMarkings() {
        return deadLockMarkings;
    }

    @Override
    public Set<M> getMarkingsWithNoOptionToComplete() {
        return markingsWithoutOptionToComplete;
    }

    @Override
    public Probability getProbabilityToComplete() {
        return getProbabilityToComplete(initialMarking);
    }

    @Override
    public Probability getProbabilityToComplete(M marking) {
        Probability probability = markingProbabilityMapping.get(marking);
        if (Objects.isNull(probability)) {
            return Probability.ZERO;
        }
        return probability;
    }

    @Override
    public ReachabilityGraph getFixedReachabilityGraph() {
        ReachabilityGraph newRg = new ReachabilityGraph(reachabilityGraph.getLabel());
        for (State node : reachabilityGraph.getNodes()) {
            if (markingsWithoutOptionToComplete.contains(node.getIdentifier())) {
                continue;
            }
            newRg.addState(node.getIdentifier());
        }

        for (Transition transition : reachabilityGraph.getEdges()) {
            if (markingsWithoutOptionToComplete.contains(transition.getSource().getIdentifier())) {
                continue;
            }

            if (markingsWithoutOptionToComplete.contains(transition.getTarget().getIdentifier())) {
                continue;
            }
            newRg.addTransition(transition.getSource().getIdentifier(), transition.getTarget().getIdentifier(), transition.getIdentifier());
        }
        return newRg;
    }

    @Override
    public String toString() {
        return String.format("modelMax: %s, noOptionToCompleteMarkings: %d, deadLockMarkings: %d", getProbabilityToComplete().toString(), getMarkingsWithNoOptionToComplete().size(), getDeadLockMarkings().size());
    }
}
