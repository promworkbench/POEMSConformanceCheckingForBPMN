package org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.statespace;

import org.processmining.models.graphbased.directed.DirectedGraphNode;
import org.processmining.models.graphbased.directed.bpmn.BPMNDiagram;
import org.processmining.models.graphbased.directed.transitionsystem.State;
import org.processmining.models.graphbased.directed.transitionsystem.TransitionSystem;
import org.processmining.models.graphbased.directed.transitionsystem.TransitionSystemImpl;
import org.processmining.petrinets.utils.DirectedGraphUtils;
import org.processmining.sccwbpmnnpos.algorithms.utils.cartesianproduct.CartesianProductCalculator;
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
import org.processmining.sccwbpmnnpos.models.bpmn.execution.path.BpmnExecutionPath;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.path.PartiallyOrderedBpmnExecutionPath;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.path.firing_event.BpmnFiringEvent;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.path.firing_event.BpmnUnavoidableLiveLockDetected;

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
    public TransitionSystem convert(BPMNDiagram bpmnDiagram) throws BpmnNoOptionToCompleteException,
            BpmnUnboundedException {
        ExecutableBpmnDiagramImpl executableDiagram = new ExecutableBpmnDiagramImpl(bpmnDiagram, nodeFactory);
        TransitionSystem transitionSystem = new TransitionSystemImpl(bpmnDiagram.getLabel());

        BpmnStateChange initialMarking = getInitialMarking(executableDiagram);
        transitionSystem.addState(initialMarking.getTargetMarking());
        BpmnMarking marking2 = executeUntilThereAreOnlyChoices(executableDiagram,
                initialMarking.getTargetMarking(), initialMarking.getEdge().getPath());
        transitionSystem.addState(marking2);
        transitionSystem.addTransition(initialMarking.getTargetMarking(), marking2, initialMarking.getEdge());

        Set<BpmnMarking> markings = new HashSet<>();
        markings.add(marking2);
        do {
            Set<BpmnMarking> tmp = new HashSet<>();
            for (BpmnMarking marking : markings) {
                transitionSystem.addState(marking);
                Set<BpmnStateChange> nextMarkings = executeNextBatch(executableDiagram, marking);
                for (BpmnStateChange nextState : nextMarkings) {
                    if (!transitionSystem.getStates().contains(nextState.getTargetMarking())) {
                        // Loop detected, don't process the same marking again
                        Set<DirectedGraphNode> preSet = DirectedGraphUtils.getPreSet(transitionSystem,
                                transitionSystem.getNode(marking));
                        Set<BpmnMarking> preMarkings =
                                preSet.stream().map(s -> (BpmnMarking) ((State) s).getIdentifier()).collect(Collectors.toSet());
                        preMarkings.add(marking);
                        for (BpmnMarking preMarking : preMarkings) {
                            if (nextState.getTargetMarking().contains(preMarking)) {
                                throw new BpmnUnboundedException(nextState.getTargetMarking(), preMarking);
                            }
                        }
                        if (!nextState.getTargetMarking().isEmpty() && nextState.isComplete()) {
                            tmp.add(nextState.getTargetMarking());
                        }
                        transitionSystem.addState(nextState.getTargetMarking());
                    }
                    transitionSystem.addTransition(marking, nextState.getTargetMarking(), nextState.getEdge());
                }
            }
            markings = tmp;
        } while (!markings.isEmpty());

        return transitionSystem;
    }

    private Set<BpmnStateChange> executeNextBatch(ExecutableBpmnDiagramImpl diagram, BpmnMarking marking) throws BpmnNoOptionToCompleteException {
        final BpmnExecutionPath path = new PartiallyOrderedBpmnExecutionPath();
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
                                                        BpmnExecutionPath path) throws BpmnNoOptionToCompleteException {
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
                    getEdge(Collections.emptyList(), new PartiallyOrderedBpmnExecutionPath())));
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
            BpmnExecutionPath path = new PartiallyOrderedBpmnExecutionPath();
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
                                             BpmnExecutionPath path) throws BpmnNodeNotEnabledException,
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
        BpmnExecutionPath path = new PartiallyOrderedBpmnExecutionPath();
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
                                    BpmnExecutionPath path) throws BpmnNodeNotEnabledException,
            BpmnNoOptionToCompleteException {
        BpmnMarking resultMarking = marking;
        for (BpmnNodeFiringOption firingOption : firingOptions) {
            BpmnFiringChange bpmnFiringChange = firingOption.getNode().fireOne(resultMarking, firingOption);
            resultMarking = bpmnFiringChange.getResultMarking();
            int sinkNodeFiringIndex = path.fire(firingOption.getNode().getNode());
            BpmnFiringEvent sinkEvent = path.getFiringEvent(firingOption.getNode().getNode(), sinkNodeFiringIndex);
            for (BpmnToken token : bpmnFiringChange.getConsumedMarking()) {
                int sourceNodeFiringIndex = path.getTimesFired(token.getSourceNode());
                if (sourceNodeFiringIndex == 0) {
                    continue;
                }
                BpmnFiringEvent sourceEvent = path.getFiringEvent(token.getSourceNode(),
                        sourceNodeFiringIndex - resultMarking.count(token));
                try {
                    path.connect(sourceEvent, sinkEvent);
                } catch (BpmnUnavoidableLiveLockDetected e) {
                    throw new BpmnNoOptionToCompleteException(bpmnFiringChange.getOriginalMarking(),
                            bpmnFiringChange.getResultMarking(), path, e);
                }
            }
        }
        return resultMarking;
    }

    protected BpmnReachabilityGraphEdge getEdge(final Collection<BpmnNodeFiringOption> firingOptions,
                                                      BpmnExecutionPath path) {
        return new BpmnReachabilityGraphEdge(path);
    }
}
