package org.processmining.sccwbpmnnpos.algorithms.inputs.reachabilitygraph;

import org.processmining.models.graphbased.directed.transitionsystem.ReachabilityGraph;
import org.processmining.models.graphbased.directed.transitionsystem.State;
import org.processmining.sccwbpmnnpos.algorithms.utils.directedGraph.DirectedGraphUtils;
import org.processmining.sccwbpmnnpos.models.execution.Marking;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ReachabilityGraphUtils {
    <E, M extends Marking<E>> Set<M> getFinalMarkings(ReachabilityGraph reachabilityGraph, Class<M> clazz) {
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

    <E, M extends Marking<E>> Set<M> getDeadlockMarkings(ReachabilityGraph reachabilityGraph, Class<M> clazz) {
        Set<State> deadlockNodes = DirectedGraphUtils.getDeadlockNodes(reachabilityGraph);
        Set<M> deadlockMarkings =
                deadlockNodes.stream().map(s -> clazz.cast(s.getIdentifier())).collect(Collectors.toSet());
        deadlockMarkings.removeAll(getFinalMarkings(reachabilityGraph, clazz));
        return deadlockMarkings;
    }

    <E, M extends Marking<E>> Set<M> getMarkingsWithNoOptionToComplete(ReachabilityGraph reachabilityGraph,
                                                                       Class<M> clazz) {
        Set<M> finalMarkings = getFinalMarkings(reachabilityGraph, clazz);
        Set<State> finalStates = finalMarkings.stream().map(reachabilityGraph::getNode).collect(Collectors.toSet());
        Set<State> allNonPredecessors = DirectedGraphUtils.getAllNonPredecessors(reachabilityGraph, finalStates);
        return allNonPredecessors.stream().map(s -> clazz.cast(s.getIdentifier())).collect(Collectors.toSet());
    }
}
