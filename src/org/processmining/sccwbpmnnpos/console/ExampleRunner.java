package org.processmining.sccwbpmnnpos.console;

import org.deckfour.xes.classification.XEventNameClassifier;
import org.deckfour.xes.model.XLog;
import org.processmining.sccwbpmnnpos.algorithms.inputs.log.simplifier.XLogSimplifier;
import org.processmining.sccwbpmnnpos.algorithms.inputs.log.simplifier.impl.BasicXLogSimplifier;
import org.processmining.sccwbpmnnpos.algorithms.inputs.log.stohastic_language.SimplifiedLogToStochasticLanguageConverter;
import org.processmining.sccwbpmnnpos.algorithms.inputs.log.stohastic_language.SimplifiedLogToStochasticLanguageConverterImpl;
import org.processmining.sccwbpmnnpos.algorithms.inputs.model.StochasticBpmnToStochasticLanguageConverter;
import org.processmining.sccwbpmnnpos.algorithms.inputs.model.StochasticBpmnToStochasticLanguageConverterImpl;
import org.processmining.sccwbpmnnpos.algorithms.utils.cartesianproduct.CartesianProductCalculator;
import org.processmining.sccwbpmnnpos.algorithms.utils.cartesianproduct.NestedLoopsCartesianProductCalculator;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.BpmnFiringChange;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.ExecutableBpmnDiagramImpl;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.firable.alternatives.BpmnNodeFiringOption;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.BpmnMarking;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.factory.BpmnMarkingFactory;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.factory.DefaultBpmnMarkingFactory;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.token.factory.BpmnTokenFactory;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.token.factory.SimpleBpmnTokenFactory;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.utils.BpmnMarkingUtils;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.utils.SimpleBpmnMarkingUtils;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.node.ExecutableBpmnNode;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.node.factory.CachedExecutableBpmnNodeFactory;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.node.factory.ExecutableBpmnNodeFactory;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.node.factory.SimpleExecutableBpmnNodeFactory;
import org.processmining.sccwbpmnnpos.models.log.SimplifiedEventLog;
import org.processmining.sccwbpmnnpos.models.stochastic_language.StochasticLanguage;
import org.processmining.sccwbpmnnpos.models.utils.activity.Activity;
import org.processmining.sccwbpmnnpos.models.utils.activity.SimpleActivityRegistry;
import org.processmining.sccwbpmnnpos.models.utils.multiset.factory.DefaultMultisetFactory;
import org.processmining.sccwbpmnnpos.models.utils.multiset.factory.MultisetFactory;
import org.processmining.sccwbpmnnpos.models.utils.multiset.utils.MultisetUtils;
import org.processmining.sccwbpmnnpos.models.utils.multiset.utils.SimpleMultisetUtils;
import org.processmining.sccwbpmnnpos.models.utils.trace.total_order.TotallyOrderedTrace;
import org.processmining.sccwbpmnnpos.utils.log.XLogReaderImpl;
import org.processmining.stochasticbpmn.algorithms.diagram.builder.StochasticBPMNDiagramBuilderImpl;
import org.processmining.stochasticbpmn.algorithms.diagram.reader.StochasticBPMNDiagramReader;
import org.processmining.stochasticbpmn.algorithms.reader.ObjectFilePathReader;
import org.processmining.stochasticbpmn.algorithms.reader.ObjectReader;
import org.processmining.stochasticbpmn.algorithms.reader.StochasticBPMNInputStreamReader;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticBPMNDiagram;

import java.util.Collection;

public class ExampleRunner {
    public static void main(String[] args) {
//        ObjectReader<String, StochasticBPMNDiagram> diagramReader = new ObjectFilePathReader<>(new StochasticBPMNDiagramReader(new StochasticBPMNInputStreamReader(), new StochasticBPMNDiagramBuilderImpl()));
//        try {
//            StochasticBPMNDiagram diagram = diagramReader.read("/home/aleks/Documents/Learn/Playground/obsidianTest/alkuzman/Research/Concepts/Process Management/Process Mining/Process Models/BPMN/Instances/Instance - BPMN - Handling of Compensation Requests.bpmn");
//            MultisetFactory multisetFactory = new DefaultMultisetFactory();
//            MultisetUtils multisetUtils = new SimpleMultisetUtils(multisetFactory);
//            BpmnTokenFactory tokenFactory = new SimpleBpmnTokenFactory();
//            BpmnMarkingFactory markingFactory = new DefaultBpmnMarkingFactory(multisetFactory);
//            BpmnMarkingUtils markingUtils = new SimpleBpmnMarkingUtils(multisetUtils, markingFactory);
//            ExecutableBpmnNodeFactory executableNodeFactory =
//                    new CachedExecutableBpmnNodeFactory(new SimpleExecutableBpmnNodeFactory(diagram, tokenFactory,
//                            markingFactory, markingUtils));
//
//            CartesianProductCalculator cartesianProductCalculator = new NestedLoopsCartesianProductCalculator();
//            StochasticBpmnToStochasticLanguageConverter bpmn2StochasticLanguageConverter = new StochasticBpmnToStochasticLanguageConverterImpl(executableNodeFactory, markingFactory, cartesianProductCalculator);
//            bpmn2StochasticLanguageConverter.convert(diagram);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }

        final ObjectReader<String, XLog> logReader = new ObjectFilePathReader<>(new XLogReaderImpl());
        final XLogSimplifier logSimplifier = new BasicXLogSimplifier(new XEventNameClassifier(), new
                SimpleActivityRegistry());
        final SimplifiedLogToStochasticLanguageConverter log2SimplifiedLanguageConverter = new
                SimplifiedLogToStochasticLanguageConverterImpl();
        try {
            final XLog log = logReader.read
            ("/home/aleks/Documents/Learn/MasterThesis/SCCwBPMN/Data/Sepsis/Log/Sepsis Cases - Event Log.xes");
            final SimplifiedEventLog simpleLog = logSimplifier.simplify(log);
            final StochasticLanguage<Activity, TotallyOrderedTrace> stochasticLanguage =
            log2SimplifiedLanguageConverter.convert(simpleLog);
            System.out.println(stochasticLanguage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
