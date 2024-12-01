package org.processmining.sccwbpmnnpos.algorithms.inputs.reachability_graph;

import org.processmining.models.graphbased.directed.transitionsystem.ReachabilityGraph;
import org.processmining.models.graphbased.directed.transitionsystem.State;
import org.processmining.models.graphbased.directed.transitionsystem.Transition;
import org.processmining.sccwbpmnnpos.algorithms.utils.directedGraph.DirectedGraphUtils;
import org.processmining.sccwbpmnnpos.models.execution.Marking;
import org.processmining.stochasticbpmn.models.stochastic.StochasticObject;

import java.math.RoundingMode;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ReachabilityGraphUtils {
    public static <M extends Marking<?>> Set<M> getFinalMarkings(ReachabilityGraph reachabilityGraph, Class<M> clazz) {
        Set<M> finalMarkings = new HashSet<>();
        for (State state : reachabilityGraph.getNodes()) {
            Object identifier = state.getIdentifier();
            M marking = clazz.cast(identifier);
            if (marking.isFinal()) {
                finalMarkings.add(marking);
            }
        }
        return finalMarkings;
    }

    public static <M extends Marking<?>> Set<M> getDeadlockMarkings(ReachabilityGraph reachabilityGraph, Class<M> clazz) {
        Set<State> deadlockNodes = DirectedGraphUtils.getDeadlockNodes(reachabilityGraph);
        Set<M> deadlockMarkings =
                deadlockNodes.stream().map(s -> clazz.cast(s.getIdentifier())).collect(Collectors.toSet());
        deadlockMarkings.removeAll(getFinalMarkings(reachabilityGraph, clazz));
        return deadlockMarkings;
    }

    public static <M extends Marking<?>> Set<M> getMarkingsWithNoOptionToComplete(ReachabilityGraph reachabilityGraph,
                                                                       Class<M> clazz) {
        Set<M> finalMarkings = getFinalMarkings(reachabilityGraph, clazz);
        Set<State> finalStates = finalMarkings.stream().map(reachabilityGraph::getNode).collect(Collectors.toSet());
        Set<State> allNonPredecessors = DirectedGraphUtils.getAllNonAscendants(reachabilityGraph, finalStates);
        return allNonPredecessors.stream().map(s -> clazz.cast(s.getIdentifier())).collect(Collectors.toSet());
    }

    public static <E, M extends Marking<E>> Set<M> getMarkingsWithOptionToComplete(ReachabilityGraph reachabilityGraph,
                                                                                     Class<M> clazz) {
        Set<M> finalMarkings = getFinalMarkings(reachabilityGraph, clazz);
        Set<State> finalStates = finalMarkings.stream().map(reachabilityGraph::getNode).collect(Collectors.toSet());
        Set<State> ascendants = DirectedGraphUtils.getAscendants(reachabilityGraph, finalStates);
        Set<M> result = ascendants.stream().map(s -> clazz.cast(s.getIdentifier())).collect(Collectors.toSet());
        result.addAll(finalMarkings);
        return result;
    }

    public static <M extends Marking<?>> Set<M> getAscendants(ReachabilityGraph reachabilityGraph, final Collection<M> startMarkings,
                                                                                   Class<M> clazz) {
        Set<State> startStates = startMarkings.stream().map(reachabilityGraph::getNode).collect(Collectors.toSet());
        Set<State> ascendants = DirectedGraphUtils.getAscendants(reachabilityGraph, startStates);
        return ascendants.stream().map(s -> clazz.cast(s.getIdentifier())).collect(Collectors.toSet());
    }

    public static <M extends Marking<?>> State getInitialState(ReachabilityGraph rg, Class<M> clazz) {
        final Set<State> initialStates = new HashSet<>();
        for (State node : rg.getNodes()) {
            M identifier = clazz.cast(node.getIdentifier());
            if (identifier.isInitial()) {
                return node;
            }
        }
        throw new IllegalStateException();
    }

    public static <M extends Marking<?>> M getInitialMarking(ReachabilityGraph rg, Class<M> clazz) {
        State initialState = getInitialState(rg, clazz);
        return clazz.cast(initialState.getIdentifier());
    }

    public static String toGraphVizString(ReachabilityGraph rg) {
        Collection<State> states = rg.getNodes();
        StringBuilder sb = new StringBuilder();
        sb.append("digraph G {");
        for (State state : states) {
            sb.append(String.format("\"%s\";\n", state.getIdentifier().toString()));
        }
        for (State state : states) {
            Collection<Transition> transitions = rg.getOutEdges(state);
            for (Transition transition : transitions) {
                State toState = transition.getTarget();
                StochasticObject so = (StochasticObject) (transition.getIdentifier());
                sb.append(String.format("\"%s\" -> \"%s\" [label=\"%s\"];\n", state.getIdentifier().toString(), toState.getIdentifier().toString(), so.getProbability().getValue().setScale(5, RoundingMode.DOWN).toString()));
            }
        }
        sb.append("}");
        return sb.toString();
    }
}
