package org.processmining.sccwbpmnnpos.algorithms.utils.directedGraph;

import org.processmining.models.graphbased.directed.DirectedGraph;
import org.processmining.models.graphbased.directed.DirectedGraphEdge;
import org.processmining.models.graphbased.directed.DirectedGraphNode;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

public class DirectedGraphUtils {
    public static <N extends DirectedGraphNode, E extends DirectedGraphEdge<N, N>> Set<N> getDeadlockNodes(final DirectedGraph<N, E> graph) {
        return graph.getNodes().stream().filter(n -> graph.getOutEdges(n).isEmpty()).collect(Collectors.toSet());
    }

    public static <N extends DirectedGraphNode, E extends DirectedGraphEdge<N, N>> Set<N> getAscendants(final DirectedGraph<N, E> graph, final Set<N> startNodes) {
        final Set<N> visited = new HashSet<>();
        final Stack<N> toVisit = new Stack<>();
        for (N reach : startNodes) {
            toVisit.push(reach);
        }

        do {
            N currentNode = toVisit.pop();
            if (visited.equals(currentNode)) {
                continue;
            }
            visited.add(currentNode);
            Collection<E> inEdges = graph.getInEdges(currentNode);
            for (E inEdge : inEdges) {
                if (!visited.contains(inEdge.getSource())) {
                    toVisit.add(inEdge.getSource());
                }
            }
        } while (!toVisit.isEmpty());
        visited.removeAll(startNodes);
        return visited;
    }

    public static <N extends DirectedGraphNode, E extends DirectedGraphEdge<N, N>> Set<N> getAllNonAscendants(final DirectedGraph<N, E> graph, final Set<N> startNodes) {
        Set<N> allNodes = graph.getNodes();
        allNodes.removeAll(getAscendants(graph, startNodes));
        allNodes.removeAll(startNodes);
        return allNodes;
    }



    public static <N extends DirectedGraphNode, E extends DirectedGraphEdge<N, N>> Set<N> getAscendants(final DirectedGraph<N, E> graph, final N node) {
        final Set<N> visited = new HashSet<>();
        final Stack<N> toVisit = new Stack<>();
        toVisit.push(node);
        do {
            N currentNode = toVisit.pop();
            if (visited.equals(currentNode)) {
                continue;
            }
            visited.add(currentNode);
            Collection<E> inEdges = graph.getInEdges(currentNode);
            for (E inEdge : inEdges) {
                if (!visited.contains(inEdge.getSource())) {
                    toVisit.add(inEdge.getSource());
                }
            }
        } while (!toVisit.isEmpty());
        visited.remove(node);
        return visited;
    }

    public static <N extends DirectedGraphNode, E extends DirectedGraphEdge<N, N>> Set<N> getDescendants(final DirectedGraph<N, E> graph, final N node) {
        final Set<N> visited = new HashSet<>();
        final Stack<N> toVisit = new Stack<>();
        toVisit.push(node);
        do {
            N currentNode = toVisit.pop();
            if (visited.equals(currentNode)) {
                continue;
            }
            visited.add(currentNode);
            Collection<E> edges = graph.getOutEdges(currentNode);
            for (E edge : edges) {
                if (!visited.contains(edge.getTarget())) {
                    toVisit.add(edge.getTarget());
                }
            }
        } while (!toVisit.isEmpty());
        visited.remove(node);
        return visited;
    }
}
