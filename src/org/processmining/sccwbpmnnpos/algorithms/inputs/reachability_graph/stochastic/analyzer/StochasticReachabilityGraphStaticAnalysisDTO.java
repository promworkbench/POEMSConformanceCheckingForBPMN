package org.processmining.sccwbpmnnpos.algorithms.inputs.reachability_graph.stochastic.analyzer;

import org.processmining.models.graphbased.directed.transitionsystem.ReachabilityGraph;
import org.processmining.models.graphbased.directed.transitionsystem.State;
import org.processmining.models.graphbased.directed.transitionsystem.Transition;
import org.processmining.plugins.graphviz.dot.Dot;
import org.processmining.plugins.graphviz.dot.DotNode;
import org.processmining.sccwbpmnnpos.models.execution.Marking;
import org.processmining.stochasticbpmn.models.stochastic.Probability;
import org.processmining.stochasticbpmn.models.stochastic.StochasticObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class StochasticReachabilityGraphStaticAnalysisDTO<M extends Marking<?>> implements StochasticReachabilityGraphStaticAnalysis<M> {
    private final ReachabilityGraph reachabilityGraph;
    private final Set<M> deadLockMarkings;
    private final Set<M> markingsWithoutOptionToComplete;
    private final Map<M, Probability> markingProbabilityMapping;
    private final M initialMarking;
    private final Class<M> clazz;

    public StochasticReachabilityGraphStaticAnalysisDTO(ReachabilityGraph reachabilityGraph, M initialMarking,
                                                        Set<M> deadLockMarkings,
                                                        Set<M> markingsWithoutOptionToComplete,
                                                        Map<M, Probability> markingProbabilityMapping, Class<M> clazz) {
        this.reachabilityGraph = reachabilityGraph;
        this.deadLockMarkings = deadLockMarkings;
        this.markingsWithoutOptionToComplete = markingsWithoutOptionToComplete;
        this.markingProbabilityMapping = markingProbabilityMapping;
        this.initialMarking = initialMarking;
        this.clazz = clazz;
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
            newRg.addTransition(transition.getSource().getIdentifier(), transition.getTarget().getIdentifier(),
                    transition.getIdentifier());
        }
        return newRg;
    }

    @Override
    public Dot toGraphViz() {
        Dot dot = new Dot();
        dot.setLabel(String.format("Probability to Complete %s", getProbabilityToComplete()));
        Collection<State> states = getReachabilityGraph().getNodes();
        Map<State, DotNode> stateMap = new HashMap<>();
        for (State state : states) {
            M marking = clazz.cast(state.getIdentifier());
            BigDecimal probability =
                    getProbabilityToComplete(marking).getValue().setScale(5,
                            RoundingMode.HALF_EVEN).stripTrailingZeros();
            String label = String.format("%s\n%s", probability, marking.toStringNewLines());
            DotNode dotNode = dot.addNode(label);
            dotNode.setSelectable(true);
            stateMap.put(state, dotNode);

            if (markingsWithoutOptionToComplete.contains(marking)) {
                dotNode.setOption("style", "filled");
                dotNode.setOption("fillcolor", "red");
            }
        }
        for (State state : states) {
            DotNode fromDotNode = stateMap.get(state);
            Collection<Transition> transitions = getReachabilityGraph().getOutEdges(state);
            for (Transition transition : transitions) {
                State toState = transition.getTarget();
                DotNode toDotNode = stateMap.get(toState);
                StochasticObject so = (StochasticObject) (transition.getIdentifier());
                BigDecimal probability = so.getProbability().getValue().setScale(5, RoundingMode.HALF_EVEN).stripTrailingZeros();
                dot.addEdge(fromDotNode, toDotNode, probability.toString());
            }
        }
        return dot;
    }

    @Override
    public String toString() {
        return String.format("maxProbability: %s, markings: %d,  noOptionToCompleteMarkings: %d, deadLockMarkings: %d",
                getProbabilityToComplete().toString(),
                getReachabilityGraph().getNodes().size(), getMarkingsWithNoOptionToComplete().size(),
                getDeadLockMarkings().size());
    }
}
