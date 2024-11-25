package org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn;

import org.processmining.sccwbpmnnpos.algorithms.utils.cartesianproduct.CartesianProductCalculator;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.ExecutableBpmnDiagramImpl;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.firable.alternatives.BpmnNodeFiringOption;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.BpmnMarking;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.factory.BpmnMarkingFactory;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.node.ExecutableBpmnNode;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.node.exceptions.BpmnNodeNotEnabledException;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.node.factory.ExecutableBpmnNodeFactory;
import org.processmining.sccwbpmnnpos.models.stochastic_language.StochasticLanguage;
import org.processmining.sccwbpmnnpos.models.utils.activity.Activity;
import org.processmining.sccwbpmnnpos.models.utils.trace.partial_order.PartiallyOrderedTrace;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticBPMNDiagram;

import java.util.*;
import java.util.stream.Collectors;

public class StochasticBpmn2StochasticLanguageConverterImpl implements StochasticBpmn2StochasticLanguageConverter {
    private final ExecutableBpmnNodeFactory nodeFactory;
    private final BpmnMarkingFactory markingFactory;
    private final CartesianProductCalculator cartesianProductCalculator;

    public StochasticBpmn2StochasticLanguageConverterImpl(ExecutableBpmnNodeFactory nodeFactory,
                                                          BpmnMarkingFactory markingFactory,
                                                          CartesianProductCalculator cartesianProductCalculator) {
        this.nodeFactory = nodeFactory;
        this.markingFactory = markingFactory;
        this.cartesianProductCalculator = cartesianProductCalculator;
    }

    @Override
    public StochasticLanguage<Activity, PartiallyOrderedTrace> convert(StochasticBPMNDiagram bpmnDiagram) {
        ExecutableBpmnDiagramImpl executableDiagram = new ExecutableBpmnDiagramImpl(bpmnDiagram, nodeFactory);
        Collection<ExecutableBpmnNode> startNodes = executableDiagram.getStartNodes();

        try {
            BpmnMarking initialMarking = executeOnce(startNodes, markingFactory.getEmpty(bpmnDiagram));
            Set<BpmnMarking> markings = new HashSet<>();
            markings.add(initialMarking);
            do {
                Set<BpmnMarking> tmp = new HashSet<>();
                for (BpmnMarking marking : markings) {
                    tmp.addAll(executeNextBatch(executableDiagram, marking));
                }
                System.out.println(markings);
            } while (markings.size() > 1 || !markings.iterator().next().isEmpty());
        } catch (BpmnNodeNotEnabledException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    private Set<BpmnMarking> executeNextBatch(ExecutableBpmnDiagramImpl diagram, BpmnMarking marking) throws BpmnNodeNotEnabledException {
        BpmnMarking resultMarking = executeUntilThereAreOnlyChoices(diagram, marking);
        if (!Objects.equals(resultMarking, marking)) {
            return Collections.singleton(resultMarking);
        }
        Set<BpmnMarking> resultMarkings = executeNextChoice(diagram, marking);
        if (resultMarkings.size() == 1 && Objects.equals(resultMarkings.iterator().next(), marking)) {
            return resultMarkings;
        }
        Set<BpmnMarking> finalMarkings = new HashSet<>();
        for (BpmnMarking bpmnTokens : resultMarkings) {
            finalMarkings.add(executeUntilThereAreOnlyChoices(diagram, bpmnTokens));
        }
        return finalMarkings;
    }

    private BpmnMarking executeUntilThereAreOnlyChoices(ExecutableBpmnDiagramImpl diagram, BpmnMarking marking) throws BpmnNodeNotEnabledException {
        Collection<ExecutableBpmnNode> nodes = diagram.getEnabledNodes(marking);
        if (nodes.isEmpty()) {
            return marking;
        }
        Collection<ExecutableBpmnNode> nonChoiceNodes =
                nodes.stream().filter(n -> !n.isChoice() || n.getFiringOptions().size() == 1).collect(Collectors.toList());
        if (nonChoiceNodes.isEmpty()) {
            return marking;
        }
        BpmnMarking nextMarking = executeNextNonChoice(nonChoiceNodes, marking);
        return executeUntilThereAreOnlyChoices(diagram, nextMarking);
    }

    private Set<BpmnMarking> executeNextChoice(ExecutableBpmnDiagramImpl diagram, BpmnMarking marking) throws BpmnNodeNotEnabledException {
        Collection<ExecutableBpmnNode> nodes = diagram.getEnabledNodes(marking);
        if (nodes.isEmpty()) {
            return Collections.singleton(marking);
        }
        List<Collection<BpmnNodeFiringOption>> options = new LinkedList<>();
        for (ExecutableBpmnNode node : nodes) {
            int count = node.getTimesEnabledIn(marking);
            for (int i = 0; i < count; i++) {
                options.add(node.getFiringOptions());
            }
        }
        List<List<BpmnNodeFiringOption>> cartesianProduct = cartesianProductCalculator.calculate(options);
        Set<BpmnMarking> result = new HashSet<>();
        for (List<BpmnNodeFiringOption> combinations : cartesianProduct) {
            BpmnMarking currentMarking = markingFactory.create(marking.getModel(), marking);
            for (BpmnNodeFiringOption firingOption : combinations) {
                currentMarking = firingOption.getNode().fireOne(currentMarking, firingOption).getResultMarking();
            }
            result.add(currentMarking);
        }
        return result;
    }

    private BpmnMarking executeNextNonChoice(Collection<ExecutableBpmnNode> nodes, BpmnMarking marking) throws BpmnNodeNotEnabledException {
        BpmnMarking resultMarking = marking;
        for (ExecutableBpmnNode node : nodes) {
            resultMarking = node.fireAll(resultMarking).getResultMarking();
        }
        return resultMarking;
    }

    private BpmnMarking executeOnce(Collection<ExecutableBpmnNode> nodes, BpmnMarking marking) throws BpmnNodeNotEnabledException {
        BpmnMarking resultMarking = marking;
        for (ExecutableBpmnNode node : nodes) {
            resultMarking = node.fireOne(marking).getResultMarking();
        }
        return resultMarking;
    }
}
