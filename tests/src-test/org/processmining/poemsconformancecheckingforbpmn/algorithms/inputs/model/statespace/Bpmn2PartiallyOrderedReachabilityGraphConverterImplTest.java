package org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.model.statespace;

import org.junit.Assert;
import org.junit.Test;
import org.processmining.models.graphbased.directed.bpmn.BPMNDiagram;
import org.processmining.models.graphbased.directed.transitionsystem.ReachabilityGraph;
import org.processmining.models.graphbased.directed.transitionsystem.TransitionSystem;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.bpmn.statespace.Bpmn2POReachabilityGraphConverterImpl;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.bpmn.statespace.Bpmn2POReachabilityGraphConverter;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.bpmn.statespace.BpmnNoOptionToCompleteException;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.bpmn.statespace.BpmnUnboundedException;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.utils.cartesianproduct.CartesianProductCalculator;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.utils.cartesianproduct.NestedLoopsCartesianProductCalculator;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.marking.factory.BpmnMarkingFactory;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.marking.factory.DefaultBpmnMarkingFactory;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.marking.token.factory.BpmnTokenFactory;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.marking.token.factory.SimpleBpmnTokenFactory;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.marking.utils.BpmnMarkingUtils;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.marking.utils.SimpleBpmnMarkingUtils;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.node.factory.CachedExecutableBpmnNodeFactory;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.node.factory.ExecutableBpmnNodeFactory;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.node.factory.SimpleExecutableBpmnNodeFactory;
import org.processmining.poemsconformancecheckingforbpmn.models.utils.multiset.factory.DefaultMultisetFactory;
import org.processmining.poemsconformancecheckingforbpmn.models.utils.multiset.factory.MultisetFactory;
import org.processmining.poemsconformancecheckingforbpmn.models.utils.multiset.utils.MultisetUtils;
import org.processmining.poemsconformancecheckingforbpmn.models.utils.multiset.utils.SimpleMultisetUtils;
import org.processmining.stochasticbpmn.algorithms.diagram.reader.BpmnDiagramReader;
import org.processmining.stochasticbpmn.algorithms.reader.ObjectReader;

public class Bpmn2PartiallyOrderedReachabilityGraphConverterImplTest {
    private static final String modelsFolder = "/home/aleks/Documents/Learn/Playground/obsidianTest/alkuzman/Research" +
            "/Concepts/Process Management/Process Mining/Process Models/BPMN/Instances/";
    private final ObjectReader<String, BPMNDiagram> diagramReader;
    private final Bpmn2POReachabilityGraphConverter bpmnReachabilityGraphCreator;

    public Bpmn2PartiallyOrderedReachabilityGraphConverterImplTest() {
        this.diagramReader = BpmnDiagramReader.fromFileName();
        MultisetFactory multisetFactory = new DefaultMultisetFactory();
        MultisetUtils multisetUtils = new SimpleMultisetUtils(multisetFactory);
        BpmnTokenFactory tokenFactory = new SimpleBpmnTokenFactory();
        BpmnMarkingFactory markingFactory = new DefaultBpmnMarkingFactory(multisetFactory);
        BpmnMarkingUtils markingUtils = new SimpleBpmnMarkingUtils(multisetUtils, markingFactory);
        ExecutableBpmnNodeFactory executableNodeFactory =
                new CachedExecutableBpmnNodeFactory(new SimpleExecutableBpmnNodeFactory(tokenFactory,
                        markingFactory, markingUtils));
        CartesianProductCalculator cartesianProductCalculator = new NestedLoopsCartesianProductCalculator();
        this.bpmnReachabilityGraphCreator =
                new Bpmn2POReachabilityGraphConverterImpl(executableNodeFactory, markingUtils,
                        cartesianProductCalculator);
    }

    private BPMNDiagram readDiagram(String fileName) throws Exception {
        return diagramReader.read(modelsFolder + fileName);
    }

    @Test
    public void handlingOfCompensationRequests() throws Exception {
        BPMNDiagram diagram = readDiagram("Instance - BPMN - Handling of Compensation Requests.bpmn");
        ReachabilityGraph rg = bpmnReachabilityGraphCreator.convert(diagram);
        Assert.assertNotNull(rg);
    }

    @Test
    public void simpleOneChoiceLoop() throws Exception {
        BPMNDiagram diagram = readDiagram("Instance - BPMN - Simple One Choice Loop.bpmn");
        ReachabilityGraph rg = bpmnReachabilityGraphCreator.convert(diagram);
        Assert.assertNotNull(rg);
    }

    @Test(expected = BpmnNoOptionToCompleteException.class)
    public void simpleLiveLockNoOptionToComplete() throws Exception {
        BPMNDiagram diagram = readDiagram("No option to complete/Live Lock/Instance - BPMN - Simple live-lock no " +
                "option to complete.bpmn");
        ReachabilityGraph rg = bpmnReachabilityGraphCreator.convert(diagram);
        Assert.assertNotNull(rg);
    }

    @Test()
    public void partiallyLiveLocked() throws Exception {
        BPMNDiagram diagram = readDiagram("No option to complete/Live Lock/Instance - BPMN - Partially live-locked" +
                ".bpmn");
        ReachabilityGraph rg = bpmnReachabilityGraphCreator.convert(diagram);
        Assert.assertNotNull(rg);
    }

    @Test(expected = BpmnUnboundedException.class)
    public void unboundedCompletely2() throws Exception {
        BPMNDiagram diagram = readDiagram("Unbounded/Instance - BPMN - Unbounded Completely 1.bpmn");
        ReachabilityGraph rg = bpmnReachabilityGraphCreator.convert(diagram);
        Assert.assertNotNull(rg);
    }

    @Test
    public void challenge2Bounded() throws Exception {
        BPMNDiagram diagram = readDiagram("Bounded/Challenging/Instance - BPMN - 2-bounded Challenging.bpmn");
        ReachabilityGraph rg = bpmnReachabilityGraphCreator.convert(diagram);
        Assert.assertNotNull(rg);
    }

    @Test
    public void challenge2Bounded2() throws Exception {
        BPMNDiagram diagram = readDiagram("Bounded/Challenging/Instance - BPMN - 2-bounded Challenging 2.bpmn");
        ReachabilityGraph rg = bpmnReachabilityGraphCreator.convert(diagram);
        Assert.assertNotNull(rg);
    }

    @Test
    public void challengeNonLoopBounded() throws Exception {
        BPMNDiagram diagram = readDiagram("Bounded/Challenging/Instance - BPMN - Non loop bounded Challenging.bpmn");
        ReachabilityGraph rg = bpmnReachabilityGraphCreator.convert(diagram);
        Assert.assertNotNull(rg);
    }

    @Test
    public void partialOrderWithRepetitions() throws Exception {
        BPMNDiagram diagram = readDiagram("Partial Order/Instance - BPMN - Partial Order with Repetitions.bpmn");
        TransitionSystem rg = bpmnReachabilityGraphCreator.convert(diagram);
        Assert.assertNotNull(rg);
    }
}