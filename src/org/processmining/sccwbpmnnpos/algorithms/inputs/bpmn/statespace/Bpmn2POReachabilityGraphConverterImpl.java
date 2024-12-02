package org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.statespace;

import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;
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
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.token.BpmnToken;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.utils.BpmnMarkingUtils;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.node.ExecutableBpmnNode;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.node.exceptions.BpmnNodeNotEnabledException;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.node.factory.ExecutableBpmnNodeFactory;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.path.BpmnPartiallyOrderedPath;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.path.BpmnPartiallyOrderedPathImpl;
import org.processmining.sccwbpmnnpos.models.utils.ordered_set.exceptions.PartialOrderLoopNotAllowedException;
import org.processmining.sccwbpmnnpos.models.utils.ordered_set.partial.eventbased.EventBasedPartiallyOrderedSet;
import org.processmining.sccwbpmnnpos.models.utils.ordered_set.partial.eventbased.EventBasedPartiallyOrderedSet.Event;
import org.processmining.sccwbpmnnpos.models.utils.ordered_set.partial.eventbased.NonRepetitiveEventBasedPartiallyOrderedSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class Bpmn2POReachabilityGraphConverterImpl implements Bpmn2POReachabilityGraphConverter {
    private static final Logger LOGGER = LoggerFactory.getLogger(Bpmn2POReachabilityGraphConverterImpl.class);
    private final ExecutableBpmnNodeFactory nodeFactory;
    private final CartesianProductCalculator cartesianProductCalculator;
    private final BpmnMarkingUtils markingUtils;

    public Bpmn2POReachabilityGraphConverterImpl(ExecutableBpmnNodeFactory nodeFactory, BpmnMarkingUtils markingUtils
            , CartesianProductCalculator cartesianProductCalculator) {
        this.nodeFactory = nodeFactory;
        this.markingUtils = markingUtils;
        this.cartesianProductCalculator = cartesianProductCalculator;
    }

    @Override
    public ReachabilityGraph convert(BPMNDiagram bpmnDiagram) throws BpmnNoOptionToCompleteException,
            BpmnUnboundedException {
        LOGGER.debug("Constructing Reachability Graph for BPMN");
        ExecutableBpmnDiagramImpl executableDiagram = new ExecutableBpmnDiagramImpl(bpmnDiagram, nodeFactory);
        ReachabilityGraph reachabilityGraph = new ReachabilityGraph(bpmnDiagram.getLabel() + " - Reachability Graph");

        BpmnStateChange initialMarking = getInitialMarking(executableDiagram);
        reachabilityGraph.addState(initialMarking.getTargetMarking());
        BpmnMarking marking2 = executeUntilThereAreOnlyChoices(executableDiagram, initialMarking.getTargetMarking(),
                initialMarking.getEdge().getPath());
        reachabilityGraph.addState(marking2);
        reachabilityGraph.addTransition(initialMarking.getTargetMarking(), marking2, initialMarking.getEdge());

        Set<BpmnMarking> markings = new HashSet<>();
        markings.add(marking2);
        do {
            Set<BpmnMarking> tmp = new HashSet<>();
            for (BpmnMarking marking : markings) {
                reachabilityGraph.addState(marking);
                Collection<BpmnStateChange> nextMarkings = executeNextBatch(executableDiagram, marking);
                for (BpmnStateChange nextState : nextMarkings) {
                    if (!reachabilityGraph.getStates().contains(nextState.getTargetMarking())) {
                        // Loop detected, don't process the same marking again
                        Set<State> preSet = DirectedGraphUtils.getAscendants(reachabilityGraph,
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

        for (State node : reachabilityGraph.getNodes()) {
            node.getAttributeMap().put("ProM_Vis_attr_showLabel", true);
        }
        LOGGER.debug("Reachability Graph for BPMN Constructed");
        return reachabilityGraph;
    }

    private Collection<BpmnStateChange> executeNextBatch(ExecutableBpmnDiagramImpl diagram, BpmnMarking marking) throws BpmnNoOptionToCompleteException {
        final BpmnPartiallyOrderedPath path = newPath();
        BpmnMarking resultMarking = executeUntilThereAreOnlyChoices(diagram, marking, path);
        if (!Objects.equals(resultMarking, marking)) {
            return Collections.singleton(new BpmnStateChange(resultMarking, getEdge(Collections.emptyList(), path, 1,
                    1)));
        }
        return executeNextChoiceBatch(diagram, marking);
    }

    private Collection<BpmnStateChange> executeNextChoiceBatch(ExecutableBpmnDiagramImpl diagram, BpmnMarking marking) {
        Collection<BpmnStateChange> resultMarkings = executeNextChoice(diagram, marking);
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
                finalMarkings.add(new BpmnStateChange(e.reachedMarking, choiceMarking.getEdge(), false));
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
                nodes.stream().filter(n -> !n.isChoice() || n.getProduceOptions().size() == 1).collect(Collectors.toList());
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

    private List<BpmnStateChange> executeNextChoice(ExecutableBpmnDiagramImpl diagram, BpmnMarking marking) {
        Collection<ExecutableBpmnNode> nodes = diagram.getEnabledNodes(marking);
        if (nodes.isEmpty()) {
            return Collections.singletonList(new BpmnStateChange(marking, getEdge(Collections.emptyList(), newPath(), 1,
                    1)));
        }
        List<Collection<BpmnNodeFiringOption>> options = new LinkedList<>();
        for (ExecutableBpmnNode node : nodes) {
            options.addAll(node.getFiringOptions(marking));
        }
        List<List<BpmnNodeFiringOption>> cartesianProduct = cartesianProductCalculator.calculate(options);
//        Map<BpmnMarking, List<BpmnNodeFiringOption>> uniqueCombinations = new HashMap<>();
//        TObjectIntMap<BpmnMarking> combinationCount = new TObjectIntHashMap<>();
//        for (List<BpmnNodeFiringOption> combination : cartesianProduct) {
//            BpmnMarking combinationMarking = markingUtils.emptyMarking(diagram.getDiagram());
//            for (BpmnNodeFiringOption firingOption : combination) {
//                combinationMarking = markingUtils.sum(combinationMarking, firingOption.getMarking());
//            }
//            uniqueCombinations.computeIfAbsent(combinationMarking,
//                    k -> combination);
//            combinationCount.adjustOrPutValue(combinationMarking, 1, 1);
//        }
        TObjectIntMap<BPMNNode> sourceNodesFired = new TObjectIntHashMap<>();
        for (BpmnToken token : marking) {
            sourceNodesFired.adjustOrPutValue(token.getSourceNode(), 1, 1);
        }

        LinkedList<BpmnStateChange> result = new LinkedList<>();
        for (List<BpmnNodeFiringOption> combination : cartesianProduct) {
            combination.sort(Comparator.comparing(f -> f.getProducesMarking().iterator().next().getSinkNode()));
            BpmnPartiallyOrderedPath path = newPath();
            for (BPMNNode node : sourceNodesFired.keySet()) {
                int node_times_fired = sourceNodesFired.get(node);
                for (int i = node_times_fired - 1; i >= 0; i--) {
                    path.fire(node, -i);
                }
            }
            BpmnMarking currentMarking = markingUtils.copy(marking);
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
//            int timesFired = combinationCount.get(combination.getKey());
            int timesFired = 1;
            result.add(new BpmnStateChange(resultMarking, getEdge(combination, path, timesFired,
                    cartesianProduct.size())));
        }
        return result;
    }

    private BpmnMarking executeNextNonChoice(Collection<ExecutableBpmnNode> nodes, BpmnMarking marking,
                                             BpmnPartiallyOrderedPath path) throws BpmnNodeNotEnabledException,
            BpmnNoOptionToCompleteException {
        Collection<BpmnNodeFiringOption> firingOptions = new LinkedList<>();
        for (ExecutableBpmnNode node : nodes) {
            firingOptions.addAll(node.getDefaultFiringOption(marking));
        }
        return executeOnce(firingOptions, marking, path);
    }

    private BpmnStateChange getInitialMarking(ExecutableBpmnDiagram diagram) throws BpmnNoOptionToCompleteException {
        Collection<ExecutableBpmnNode> nodes = diagram.getStartNodes();
        BpmnPartiallyOrderedPath path = newPath();
        List<BpmnNodeFiringOption> options =
                nodes.stream().flatMap(n -> n.getDefaultFiringOption(markingUtils.emptyMarking(diagram.getDiagram())).stream()).collect(Collectors.toList());
        BpmnMarking initialMarking = null;
        try {
            initialMarking = executeOnce(options, markingUtils.emptyMarking(diagram.getDiagram()), path);
        } catch (BpmnNodeNotEnabledException e) {
            // This should not happen because we start with enabled nodes.
            throw new RuntimeException(e);
        }
        return new BpmnStateChange(initialMarking, getEdge(options, path, 1, 1));
    }

    private BpmnMarking executeOnce(Collection<BpmnNodeFiringOption> firingOptions, BpmnMarking marking,
                                    BpmnPartiallyOrderedPath path) throws BpmnNodeNotEnabledException,
            BpmnNoOptionToCompleteException {
        BpmnMarking resultMarking = marking;
        for (BpmnNodeFiringOption firingOption : firingOptions) {
            BpmnFiringChange bpmnFiringChange = firingOption.getNode().fireOne(resultMarking, firingOption);
            resultMarking = bpmnFiringChange.getResultMarking();
            EventBasedPartiallyOrderedSet.Event<BPMNNode> sinkEvent = path.fire(firingOption.getNode().getNode());
            for (BpmnToken token : firingOption.getConsumesMarking()) {
                BPMNNode sourceNode = token.getSourceNode();
                ExecutableBpmnNode sourceFiringNode = nodeFactory.create(sourceNode);
                int sourceNodeFiringIndex;
                if (sourceFiringNode.getProducesTokensCount() > 1) {
                    // Independent
                    sourceNodeFiringIndex = path.getTimesFired(sourceNode) - resultMarking.count(token);
                } else {
                    sourceNodeFiringIndex = path.getTimesFired(sourceNode) - resultMarking.nodeProducedTokensCount(sourceNode);
                }
                if (sourceNodeFiringIndex < 0) {
                    continue;
                }
                EventBasedPartiallyOrderedSet.Event<BPMNNode> sourceEvent = path.getFiringEvent(sourceNode,
                        sourceNodeFiringIndex);
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
                                                BpmnPartiallyOrderedPath path, int firedTimes, int numOfOptions) {
        return new BpmnReachabilityGraphEdge(path);
    }

    private BpmnPartiallyOrderedPath newPath() {
        return new BpmnPartiallyOrderedPathImpl(new NonRepetitiveEventBasedPartiallyOrderedSet<>());
    }
}