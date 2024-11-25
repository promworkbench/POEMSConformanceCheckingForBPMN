package org.processmining.sccwbpmnnpos.console;

import org.processmining.models.graphbased.directed.bpmn.BPMNDiagram;
import org.processmining.models.graphbased.directed.transitionsystem.TransitionSystem;
import org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.statespace.Bpmn2ReachabilityGraphConverter;
import org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.statespace.Bpmn2PartiallyOrderedReachabilityGraphConverterImpl;
import org.processmining.sccwbpmnnpos.algorithms.utils.cartesianproduct.CartesianProductCalculator;
import org.processmining.sccwbpmnnpos.algorithms.utils.cartesianproduct.NestedLoopsCartesianProductCalculator;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.factory.BpmnMarkingFactory;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.factory.DefaultBpmnMarkingFactory;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.token.factory.BpmnTokenFactory;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.token.factory.SimpleBpmnTokenFactory;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.utils.BpmnMarkingUtils;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.utils.SimpleBpmnMarkingUtils;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.node.factory.CachedExecutableBpmnNodeFactory;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.node.factory.ExecutableBpmnNodeFactory;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.node.factory.SimpleExecutableBpmnNodeFactory;
import org.processmining.sccwbpmnnpos.models.utils.multiset.factory.DefaultMultisetFactory;
import org.processmining.sccwbpmnnpos.models.utils.multiset.factory.MultisetFactory;
import org.processmining.sccwbpmnnpos.models.utils.multiset.utils.MultisetUtils;
import org.processmining.sccwbpmnnpos.models.utils.multiset.utils.SimpleMultisetUtils;
import org.processmining.stochasticbpmn.algorithms.diagram.builder.BpmnDiagramBuilderImpl;
import org.processmining.stochasticbpmn.algorithms.diagram.reader.BpmnDiagramReader;
import org.processmining.stochasticbpmn.algorithms.reader.BpmnInputStreamReader;
import org.processmining.stochasticbpmn.algorithms.reader.ObjectFilePathReader;
import org.processmining.stochasticbpmn.algorithms.reader.ObjectReader;

public class ExampleRunner {
    public static void main(String[] args) {
        MultisetFactory multisetFactory = new DefaultMultisetFactory();
        MultisetUtils multisetUtils = new SimpleMultisetUtils(multisetFactory);
        BpmnTokenFactory tokenFactory = new SimpleBpmnTokenFactory();
        BpmnMarkingFactory markingFactory = new DefaultBpmnMarkingFactory(multisetFactory);
        BpmnMarkingUtils markingUtils = new SimpleBpmnMarkingUtils(multisetUtils, markingFactory);
        ExecutableBpmnNodeFactory executableNodeFactory =
                new CachedExecutableBpmnNodeFactory(new SimpleExecutableBpmnNodeFactory(tokenFactory,
                        markingFactory, markingUtils));

        CartesianProductCalculator cartesianProductCalculator = new NestedLoopsCartesianProductCalculator();
        Bpmn2ReachabilityGraphConverter bpmn2StochasticLanguageConverter = new Bpmn2PartiallyOrderedReachabilityGraphConverterImpl(executableNodeFactory, markingFactory, cartesianProductCalculator);
        ObjectReader<String, BPMNDiagram> diagramReader = new ObjectFilePathReader<>(new BpmnDiagramReader(new BpmnInputStreamReader(), new BpmnDiagramBuilderImpl()));
        try {
            BPMNDiagram diagram = diagramReader.read("/home/aleks/Documents/Learn/Playground/obsidianTest/alkuzman/Research/Concepts/Process Management/Process Mining/Process Models/BPMN/Instances/Unbounded/Instance - BPMN - Unbounded Completely 1.bpmn");
            TransitionSystem stateSpace = bpmn2StochasticLanguageConverter.convert(diagram);
            System.out.println(stateSpace);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

//        final ObjectReader<String, XLog> logReader = new ObjectFilePathReader<>(new XLogReaderImpl());
//        final XLogSimplifier logSimplifier = new BasicXLogSimplifier(new XEventNameClassifier(), new
//                SimpleActivityRegistry());
//        final SimplifiedLogToStochasticLanguageConverter log2SimplifiedLanguageConverter = new
//                SimplifiedLogToStochasticLanguageConverterImpl();
//        try {
//            final XLog log = logReader.read
//            ("/home/aleks/Documents/Learn/MasterThesis/SCCwBPMN/Data/Sepsis/Log/Sepsis Cases - Event Log.xes");
//            final SimplifiedEventLog simpleLog = logSimplifier.simplify(log);
//            final StochasticLanguage<Activity, TotallyOrderedTrace> stochasticLanguage =
//            log2SimplifiedLanguageConverter.convert(simpleLog);
//            System.out.println(stochasticLanguage);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
    }
}
