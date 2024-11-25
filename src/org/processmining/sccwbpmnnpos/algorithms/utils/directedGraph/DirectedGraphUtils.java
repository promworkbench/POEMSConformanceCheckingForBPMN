package org.processmining.sccwbpmnnpos.algorithms.utils.directedGraph;

import org.processmining.models.graphbased.directed.DirectedGraph;
import org.processmining.models.graphbased.directed.DirectedGraphEdge;
import org.processmining.models.graphbased.directed.DirectedGraphNode;

import javax.xml.soap.Node;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

public class DirectedGraphUtils {
    public static <N extends DirectedGraphNode, E extends DirectedGraphEdge<N, N>> Set<N> getDeadlockNodes(final DirectedGraph<N, E> graph) {
        return graph.getNodes().stream().filter(n -> graph.getOutEdges(n).isEmpty()).collect(Collectors.toSet());
    }

    public static <N extends DirectedGraphNode, E extends DirectedGraphEdge<N, N>> Set<N> getAllPredecessors(final DirectedGraph<N, E> graph, final Set<N> startNodes) {
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
        return visited;
    }

    public static <N extends DirectedGraphNode, E extends DirectedGraphEdge<N, N>> Set<N> getAllNonPredecessors(final DirectedGraph<N, E> graph, final Set<N> startNodes) {
        Set<N> allNodes = graph.getNodes();
        allNodes.removeAll(getAllPredecessors(graph, startNodes));
        return allNodes;
    }

    public static <N extends DirectedGraphNode, E extends DirectedGraphEdge<N, N>> Set<N> getAllPredecessors(final DirectedGraph<N, E> graph, final N node) {
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

    public static <N extends DirectedGraphNode, E extends DirectedGraphEdge<N, N>> Set<N> getAllDescendants(final DirectedGraph<N, E> graph, final N node) {
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
