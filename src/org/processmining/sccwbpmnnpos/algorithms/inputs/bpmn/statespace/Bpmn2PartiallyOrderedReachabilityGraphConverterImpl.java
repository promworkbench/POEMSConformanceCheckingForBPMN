package org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.statespace;

import org.processmining.models.graphbased.directed.bpmn.BPMNDiagram;
import org.processmining.models.graphbased.directed.bpmn.BPMNNode;
import org.processmining.models.graphbased.directed.transitionsystem.ReachabilityGraph;
import org.processmining.models.graphbased.directed.transitionsystem.State;
import org.processmining.sccwbpmnnpos.algorithms.utils.cartesianproduct.CartesianProductCalculator;
import org.processmining.sccwbpmnnpos.algorithms.utils.directedGraph.DirectedGraphUtils;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.BpmnFiringChange;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.ExecutableBpmnDiagram;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.ExecutableBpmnDiagramImpl;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.firable.alternatives.BpmnNodeFiringOption;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.BpmnMarking;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.factory.BpmnMarkingFactory;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.token.BpmnToken;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.node.ExecutableBpmnNode;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.node.exceptions.BpmnNodeNotEnabledException;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.node.factory.ExecutableBpmnNodeFactory;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.path.BpmnPartiallyOrderedPath;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.path.BpmnPartiallyOrderedPathImpl;
import org.processmining.sccwbpmnnpos.models.utils.ordered_set.exceptions.PartialOrderLoopNotAllowedException;
import org.processmining.sccwbpmnnpos.models.utils.ordered_set.partial.eventbased.EventBasedPartiallyOrderedSet;
import org.processmining.sccwbpmnnpos.models.utils.ordered_set.partial.eventbased.NonRepetitiveEventBasedPartiallyOrderedSet;

import java.util.*;
import java.util.stream.Collectors;

public class Bpmn2PartiallyOrderedReachabilityGraphConverterImpl implements Bpmn2ReachabilityGraphConverter {
    private final ExecutableBpmnNodeFactory nodeFactory;
    private final BpmnMarkingFactory markingFactory;
    private final CartesianProductCalculator cartesianProductCalculator;

    public Bpmn2PartiallyOrderedReachabilityGraphConverterImpl(ExecutableBpmnNodeFactory nodeFactory,
                                                               BpmnMarkingFactory markingFactory,
                                                               CartesianProductCalculator cartesianProductCalculator) {
        this.nodeFactory = nodeFactory;
        this.markingFactory = markingFactory;
        this.cartesianProductCalculator = cartesianProductCalculator;
    }


    @Override
    public ReachabilityGraph convert(BPMNDiagram bpmnDiagram) throws BpmnNoOptionToCompleteException,
            BpmnUnboundedException {
        ExecutableBpmnDiagramImpl executableDiagram = new ExecutableBpmnDiagramImpl(bpmnDiagram, nodeFactory);
        ReachabilityGraph reachabilityGraph = new ReachabilityGraph(bpmnDiagram.getLabel());

        BpmnStateChange initialMarking = getInitialMarking(executableDiagram);
        reachabilityGraph.addState(initialMarking.getTargetMarking());
        BpmnMarking marking2 = executeUntilThereAreOnlyChoices(executableDiagram,
                initialMarking.getTargetMarking(), initialMarking.getEdge().getPath());
        reachabilityGraph.addState(marking2);
        reachabilityGraph.addTransition(initialMarking.getTargetMarking(), marking2, initialMarking.getEdge());

        Set<BpmnMarking> markings = new HashSet<>();
        markings.add(marking2);
        do {
            Set<BpmnMarking> tmp = new HashSet<>();
            for (BpmnMarking marking : markings) {
                reachabilityGraph.addState(marking);
                Set<BpmnStateChange> nextMarkings = executeNextBatch(executableDiagram, marking);
                for (BpmnStateChange nextState : nextMarkings) {
                    if (!reachabilityGraph.getStates().contains(nextState.getTargetMarking())) {
                        // Loop detected, don't process the same marking again
                        Set<State> preSet = DirectedGraphUtils.getAllPredecessors(reachabilityGraph,
                                reachabilityGraph.getNode(marking));
                        Set<BpmnMarking> preMarkings =
                                preSet.stream().map(s -> (BpmnMarking) s.getIdentifier()).collect(Collectors.toSet());
                        preMarkings.add(marking);
                        for (BpmnMarking preMarking : preMarkings) {
                            if (nextState.getTargetMarking().contains(preMarking)) {
                                throw new BpmnUnboundedException(nextState.getTargetMarking(), preMarking);
                            }
                        }
                        if (!nextState.getTargetMarking().isEmpty() && nextState.isComplete()) {
                            tmp.add(nextState.getTargetMarking());
                        }
                        reachabilityGraph.addState(nextState.getTargetMarking());
                    }
                    reachabilityGraph.addTransition(marking, nextState.getTargetMarking(), nextState.getEdge());
                }
            }
            markings = tmp;
        } while (!markings.isEmpty());

        return reachabilityGraph;
    }

    private Set<BpmnStateChange> executeNextBatch(ExecutableBpmnDiagramImpl diagram, BpmnMarking marking) throws BpmnNoOptionToCompleteException {
        final BpmnPartiallyOrderedPath path = newPath();
        BpmnMarking resultMarking = executeUntilThereAreOnlyChoices(diagram, marking, path);
        if (!Objects.equals(resultMarking, marking)) {
            return Collections.singleton(new BpmnStateChange(resultMarking, getEdge(Collections.emptyList(), path)));
        }
        return executeNextChoiceBatch(diagram, marking);
    }

    private Set<BpmnStateChange> executeNextChoiceBatch(ExecutableBpmnDiagramImpl diagram,
                                                        BpmnMarking marking) {
        Set<BpmnStateChange> resultMarkings = executeNextChoice(diagram, marking);
        if (resultMarkings.size() == 1 && Objects.equals(resultMarkings.iterator().next().getTargetMarking(),
                marking)) {
            return resultMarkings;
        }
        Set<BpmnStateChange> finalMarkings = new HashSet<>();
        for (BpmnStateChange choiceMarking : resultMarkings) {
            BpmnMarking newMarking = null;
            try {
                newMarking = executeUntilThereAreOnlyChoices(diagram, choiceMarking.getTargetMarking(),
                        choiceMarking.getEdge().getPath());
            } catch (BpmnNoOptionToCompleteException e) {
                finalMarkings.add(new BpmnStateChange(e.reachedMarking, choiceMarking.getEdge(),
                        false));
            }
            finalMarkings.add(new BpmnStateChange(newMarking, choiceMarking.getEdge()));
        }
        return finalMarkings;
    }

    private BpmnMarking executeUntilThereAreOnlyChoices(ExecutableBpmnDiagramImpl diagram, BpmnMarking marking,
                                                        BpmnPartiallyOrderedPath path) throws BpmnNoOptionToCompleteException {
        Collection<ExecutableBpmnNode> nodes = diagram.getEnabledNodes(marking);
        if (nodes.isEmpty()) {
            return marking;
        }
        Collection<ExecutableBpmnNode> nonChoiceNodes =
                nodes.stream().filter(n -> !n.isChoice() || n.getFiringOptions().size() == 1).collect(Collectors.toList());
        if (nonChoiceNodes.isEmpty()) {
            return marking;
        }
        BpmnMarking nextMarking = null;
        try {
            nextMarking = executeNextNonChoice(nonChoiceNodes, marking, path);
        } catch (BpmnNodeNotEnabledException e) {
            // This should not happen because we start with enabled nodes.
            throw new RuntimeException(e);
        }
        return executeUntilThereAreOnlyChoices(diagram, nextMarking, path);
    }

    private Set<BpmnStateChange> executeNextChoice(ExecutableBpmnDiagramImpl diagram, BpmnMarking marking) {
        Collection<ExecutableBpmnNode> nodes = diagram.getEnabledNodes(marking);
        if (nodes.isEmpty()) {
            return Collections.singleton(new BpmnStateChange(marking,
                    getEdge(Collections.emptyList(), newPath())));
        }
        List<Collection<BpmnNodeFiringOption>> options = new LinkedList<>();
        for (ExecutableBpmnNode node : nodes) {
            int count = node.getTimesEnabledIn(marking);
            for (int i = 0; i < count; i++) {
                options.add(node.getFiringOptions());
            }
        }
        List<List<BpmnNodeFiringOption>> cartesianProduct = cartesianProductCalculator.calculate(options);
        Set<BpmnStateChange> result = new HashSet<>();
        for (List<BpmnNodeFiringOption> combination : cartesianProduct) {
            BpmnPartiallyOrderedPath path = newPath();
            BpmnMarking currentMarking = markingFactory.create(marking.getModel(), marking);
            BpmnMarking resultMarking;
            try {
                resultMarking = executeOnce(combination, currentMarking, path);
            } catch (BpmnNodeNotEnabledException e) {
                // This should not happen because we start with enabled nodes.
                throw new RuntimeException(e);
            } catch (BpmnNoOptionToCompleteException e) {
                // This should not happen it makes no sense gates to point to themselves.
                throw new RuntimeException(e);
            }
            result.add(new BpmnStateChange(resultMarking, getEdge(combination, path)));
        }
        return result;
    }

    private BpmnMarking executeNextNonChoice(Collection<ExecutableBpmnNode> nodes, BpmnMarking marking,
                                             BpmnPartiallyOrderedPath path) throws BpmnNodeNotEnabledException,
            BpmnNoOptionToCompleteException {
        Collection<BpmnNodeFiringOption> firingOptions = new LinkedList<>();
        for (ExecutableBpmnNode node : nodes) {
            for (BpmnNodeFiringOption firingOption : node.getFiringOptions()) {
                int timesEnabled = node.getTimesEnabledIn(marking);
                for (int i = 0; i < timesEnabled; i++) {
                    firingOptions.add(firingOption);
                }
            }
        }
        return executeOnce(firingOptions, marking, path);
    }

    private BpmnStateChange getInitialMarking(ExecutableBpmnDiagram diagram) throws BpmnNoOptionToCompleteException {
        Collection<ExecutableBpmnNode> nodes = diagram.getStartNodes();
        BpmnPartiallyOrderedPath path = newPath();
        List<BpmnNodeFiringOption> options =
                nodes.stream().flatMap(n -> n.getFiringOptions().stream()).collect(Collectors.toList());
        BpmnMarking initialMarking = null;
        try {
            initialMarking = executeOnce(options, markingFactory.getEmpty(diagram.getDiagram()), path);
        } catch (BpmnNodeNotEnabledException e) {
            // This should not happen because we start with enabled nodes.
            throw new RuntimeException(e);
        }
        return new BpmnStateChange(initialMarking, getEdge(options, path));
    }

    private BpmnMarking executeOnce(Collection<BpmnNodeFiringOption> firingOptions, BpmnMarking marking,
                                    BpmnPartiallyOrderedPath path) throws BpmnNodeNotEnabledException,
            BpmnNoOptionToCompleteException {
        BpmnMarking resultMarking = marking;
        for (BpmnNodeFiringOption firingOption : firingOptions) {
            BpmnFiringChange bpmnFiringChange = firingOption.getNode().fireOne(resultMarking, firingOption);
            resultMarking = bpmnFiringChange.getResultMarking();
            EventBasedPartiallyOrderedSet.Event<BPMNNode> sinkEvent = path.fire(firingOption.getNode().getNode());
            for (BpmnToken token : bpmnFiringChange.getConsumedMarking()) {
                int sourceNodeFiringIndex = path.getTimesFired(token.getSourceNode());
                if (sourceNodeFiringIndex == 0) {
                    continue;
                }
                EventBasedPartiallyOrderedSet.Event<BPMNNode> sourceEvent =
                        path.getFiringEvent(token.getSourceNode(),
                        sourceNodeFiringIndex - resultMarking.count(token));
                try {
                    path.connect(sourceEvent, sinkEvent);
                } catch (PartialOrderLoopNotAllowedException e) {
                    throw new BpmnNoOptionToCompleteException(bpmnFiringChange.getOriginalMarking(),
                            bpmnFiringChange.getResultMarking(), path, e);
                }
            }
        }
        return resultMarking;
    }

    protected BpmnReachabilityGraphEdge getEdge(final Collection<BpmnNodeFiringOption> firingOptions,
                                                BpmnPartiallyOrderedPath path) {
        return new BpmnReachabilityGraphEdge(path);
    }

    private BpmnPartiallyOrderedPath newPath() {
        return new BpmnPartiallyOrderedPathImpl(new NonRepetitiveEventBasedPartiallyOrderedSet<>());
    }
}