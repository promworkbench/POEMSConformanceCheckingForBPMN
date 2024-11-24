package org.processmining.sccwbpmnnpos.algorithms.inputs.model.statespace;

import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import org.processmining.models.graphbased.directed.bpmn.BPMNDiagram;
import org.processmining.models.graphbased.directed.transitionsystem.TransitionSystem;
import org.processmining.models.graphbased.directed.transitionsystem.TransitionSystemImpl;
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

public class Bpmn2MinimalReachabilityGraphConverterImpl implements Bpmn2MinimalReachabilityGraphConverter {
    private final ExecutableBpmnNodeFactory nodeFactory;
    private final BpmnMarkingFactory markingFactory;
    private final CartesianProductCalculator cartesianProductCalculator;

    public Bpmn2MinimalReachabilityGraphConverterImpl(ExecutableBpmnNodeFactory nodeFactory,
                                                      BpmnMarkingFactory markingFactory,
                                                      CartesianProductCalculator cartesianProductCalculator) {
        this.nodeFactory = nodeFactory;
        this.markingFactory = markingFactory;
        this.cartesianProductCalculator = cartesianProductCalculator;
    }


    @Override
    public TransitionSystem convert(BPMNDiagram bpmnDiagram) throws BpmnUnboundedException {
        ExecutableBpmnDiagramImpl executableDiagram = new ExecutableBpmnDiagramImpl(bpmnDiagram, nodeFactory);
        TransitionSystem transitionSystem = new TransitionSystemImpl(bpmnDiagram.getLabel());

        try {
            StochasticBpmnStateChange initialMarking = getInitialMarking(executableDiagram);
            transitionSystem.addState(initialMarking.getTargetMarking());
            BpmnMarking marking2 = executeUntilThereAreOnlyChoices(executableDiagram,
                    initialMarking.getTargetMarking(), initialMarking.getPath());
            transitionSystem.addState(marking2);
            transitionSystem.addTransition(initialMarking.getTargetMarking(), marking2, initialMarking.getPath());

            Set<BpmnMarking> markings = new HashSet<>();
            markings.add(marking2);
            do {
                Set<BpmnMarking> tmp = new HashSet<>();
                for (BpmnMarking marking : markings) {
                    transitionSystem.addState(marking);
                    Set<StochasticBpmnStateChange> nextMarkings = executeNextBatch(executableDiagram, marking);
                    for (StochasticBpmnStateChange nextState : nextMarkings) {
                        if (!transitionSystem.getStates().contains(nextState.getTargetMarking())) {
                            // Loop detected, don't process the same marking again
                            for (Object state : transitionSystem.getStates()) {
                                if (nextState.getTargetMarking().contains((BpmnMarking) state)) {
                                    throw new BpmnUnboundedException((BpmnMarking) state, nextState.getTargetMarking());
                                }
                            }
                            if (!nextState.getTargetMarking().isEmpty()) {
                                tmp.add(nextState.getTargetMarking());
                            }
                            transitionSystem.addState(nextState.getTargetMarking());
                        }
                        transitionSystem.addTransition(marking, nextState.getTargetMarking(), nextState.getPath());
                    }
                }
                markings = tmp;
            } while (!markings.isEmpty());
        } catch (BpmnNodeNotEnabledException e) {
            throw new RuntimeException(e);
        }

        return transitionSystem;
    }

    private Set<StochasticBpmnStateChange> executeNextBatch(ExecutableBpmnDiagramImpl diagram, BpmnMarking marking) throws BpmnNodeNotEnabledException, BpmnUnboundedException {
        final BpmnExecutionPath path = new PartiallyOrderedBpmnExecutionPath();
        BpmnMarking resultMarking = executeUntilThereAreOnlyChoices(diagram, marking, path);
        if (!Objects.equals(resultMarking, marking)) {
            return Collections.singleton(new StochasticBpmnStateChange(resultMarking, path, 1.0));
        }
        Set<StochasticBpmnStateChange> resultMarkings = executeNextChoice(diagram, marking);
        if (resultMarkings.size() == 1 && Objects.equals(resultMarkings.iterator().next(), marking)) {
            return Collections.singleton(new StochasticBpmnStateChange(marking,
                    new PartiallyOrderedBpmnExecutionPath(), 1.0));
        }
        Set<StochasticBpmnStateChange> finalMarkings = new HashSet<>();
        for (StochasticBpmnStateChange choiceMarking : resultMarkings) {
            BpmnMarking newMarking = executeUntilThereAreOnlyChoices(diagram, choiceMarking.getTargetMarking(),
                    choiceMarking.getPath());
            finalMarkings.add(new StochasticBpmnStateChange(newMarking, choiceMarking.getPath(),
                    choiceMarking.getWeight()));
        }
        return finalMarkings;
    }

    private BpmnMarking executeUntilThereAreOnlyChoices(ExecutableBpmnDiagramImpl diagram, BpmnMarking marking,
                                                        BpmnExecutionPath path) throws BpmnNodeNotEnabledException,
            BpmnUnboundedException {
        Collection<ExecutableBpmnNode> nodes = diagram.getEnabledNodes(marking);
        if (nodes.isEmpty()) {
            return marking;
        }
        Collection<ExecutableBpmnNode> nonChoiceNodes =
                nodes.stream().filter(n -> !n.isChoice() || n.getFiringOptions().size() == 1).collect(Collectors.toList());
        if (nonChoiceNodes.isEmpty()) {
            return marking;
        }
        BpmnMarking nextMarking = executeNextNonChoice(nonChoiceNodes, marking, path);
        return executeUntilThereAreOnlyChoices(diagram, nextMarking, path);
    }

    private Set<StochasticBpmnStateChange> executeNextChoice(ExecutableBpmnDiagramImpl diagram, BpmnMarking marking) throws BpmnNodeNotEnabledException, BpmnUnboundedException {
        Collection<ExecutableBpmnNode> nodes = diagram.getEnabledNodes(marking);
        if (nodes.isEmpty()) {
            return Collections.singleton(new StochasticBpmnStateChange(marking,
                    new PartiallyOrderedBpmnExecutionPath(), 1.0));
        }
        List<Collection<BpmnNodeFiringOption>> options = new LinkedList<>();
        for (ExecutableBpmnNode node : nodes) {
            int count = node.getTimesEnabledIn(marking);
            for (int i = 0; i < count; i++) {
                options.add(node.getFiringOptions());
            }
        }
        List<List<BpmnNodeFiringOption>> cartesianProduct = cartesianProductCalculator.calculate(options);
        Set<StochasticBpmnStateChange> result = new HashSet<>();
        for (List<BpmnNodeFiringOption> combination : cartesianProduct) {
            BpmnExecutionPath path = new PartiallyOrderedBpmnExecutionPath();
            BpmnMarking currentMarking = markingFactory.create(marking.getModel(), marking);
            BpmnMarking resultMarking = executeOnce(combination, currentMarking, path);
            result.add(new StochasticBpmnStateChange(resultMarking, path, 1.0));
        }
        return result;
    }

    private BpmnMarking executeNextNonChoice(Collection<ExecutableBpmnNode> nodes, BpmnMarking marking,
                                             BpmnExecutionPath path) throws BpmnNodeNotEnabledException,
            BpmnUnboundedException {
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

    private StochasticBpmnStateChange getInitialMarking(ExecutableBpmnDiagram diagram) throws BpmnNodeNotEnabledException, BpmnUnboundedException {
        Collection<ExecutableBpmnNode> nodes = diagram.getStartNodes();
        BpmnExecutionPath path = new PartiallyOrderedBpmnExecutionPath();
        List<BpmnNodeFiringOption> options =
                nodes.stream().flatMap(n -> n.getFiringOptions().stream()).collect(Collectors.toList());
        BpmnMarking initialMarking = executeOnce(options, markingFactory.getEmpty(diagram.getDiagram()), path);
        return new StochasticBpmnStateChange(initialMarking, path, 1.0);
    }

    private BpmnMarking executeOnce(Collection<BpmnNodeFiringOption> firingOptions, BpmnMarking marking,
                                    BpmnExecutionPath path) throws BpmnNodeNotEnabledException,
            BpmnUnboundedException {
        BpmnMarking resultMarking = marking;
        TObjectIntMap<BpmnNodeFiringOption> firingOptionsMap = new TObjectIntHashMap<>();
        for (BpmnNodeFiringOption firingOption : firingOptions) {
            BpmnFiringChange bpmnFiringChange = firingOption.getNode().fireOne(resultMarking, firingOption);
            resultMarking = bpmnFiringChange.getResultMarking();
            int sinkNodeFiringIndex = path.fire(firingOption.getNode().getNode());
            BpmnFiringEvent sinkEvent = path.getFiringEvent(firingOption.getNode().getNode(),
                    sinkNodeFiringIndex);
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
                    throw new BpmnUnboundedException(bpmnFiringChange.getOriginalMarking(),
                            bpmnFiringChange.getResultMarking(), e);
                }
            }
        }
        return resultMarking;
    }
}
