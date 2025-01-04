package org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.reachability_graph.analyzer;

import org.processmining.models.graphbased.directed.transitionsystem.ReachabilityGraph;
import org.processmining.models.graphbased.directed.transitionsystem.State;
import org.processmining.models.graphbased.directed.transitionsystem.Transition;
import org.processmining.plugins.graphviz.dot.Dot;
import org.processmining.plugins.graphviz.dot.DotCluster;
import org.processmining.plugins.graphviz.dot.DotNode;
import org.processmining.poemsconformancecheckingforbpmn.models.execution.Marking;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ReachabilityGraphStaticAnalysisDTO<M extends Marking<?>> implements ReachabilityGraphStaticAnalysis<M> {
    private final ReachabilityGraph reachabilityGraph;
    private final Set<M> deadLockMarkings;
    private final Set<M> markingsWithoutOptionToComplete;
    private final M initialMarking;
    private final Class<M> clazz;

    public ReachabilityGraphStaticAnalysisDTO(ReachabilityGraph reachabilityGraph, M initialMarking,
                                              Set<M> deadLockMarkings,
                                              Set<M> markingsWithoutOptionToComplete, Class<M> clazz) {
        this.reachabilityGraph = reachabilityGraph;
        this.deadLockMarkings = deadLockMarkings;
        this.markingsWithoutOptionToComplete = markingsWithoutOptionToComplete;
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
        DotCluster dotCluster = dot.addCluster();
        dotCluster.setLabel(String.format(reachabilityGraph.getLabel()));
        Collection<State> states = getReachabilityGraph().getNodes();
        Map<State, DotNode> stateMap = new HashMap<>();
        for (State state : states) {
            M marking = clazz.cast(state.getIdentifier());
            String label = String.format("%s", marking.toStringNewLines());
            DotNode dotNode = dotCluster.addNode(label);
            dotNode.setSelectable(true);
            stateMap.put(state, dotNode);

            if (markingsWithoutOptionToComplete.contains(marking)) {
                dotNode.setOption("style", "filled");
                dotNode.setOption("fillcolor", "red");
            } else {
                dotNode.setOption("style", "filled");
                dotNode.setOption("fillcolor", "white");
            }
        }
        for (State state : states) {
            DotNode fromDotNode = stateMap.get(state);
            Collection<Transition> transitions = getReachabilityGraph().getOutEdges(state);
            for (Transition transition : transitions) {
                State toState = transition.getTarget();
                DotNode toDotNode = stateMap.get(toState);
                dotCluster.addEdge(fromDotNode, toDotNode, transition.getIdentifier().toString());
            }
        }
        return dot;
    }

    @Override
    public String toString() {
        return String.format("noOptionToCompleteMarkings: %d, deadLockMarkings: %d",
                getMarkingsWithNoOptionToComplete().size(),
                getDeadLockMarkings().size());
    }
}
