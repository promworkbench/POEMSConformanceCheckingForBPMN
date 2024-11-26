package org.processmining.sccwbpmnnpos.console;

import org.processmining.models.graphbased.directed.transitionsystem.ReachabilityGraph;
import org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.stochastic.statespace.StochasticBpmn2PartiallyOrderedReachabilityGraphConverter;
import org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.stochastic.statespace.StochasticBpmn2ReachabilityGraphConverter;
import org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.stochastic.statespace.language.StochasticBpmnRG2StochasticLanguagePathConverterImpl;
import org.processmining.sccwbpmnnpos.algorithms.utils.cartesianproduct.CartesianProductCalculator;
import org.processmining.sccwbpmnnpos.algorithms.utils.cartesianproduct.NestedLoopsCartesianProductCalculator;
import org.processmining.sccwbpmnnpos.algorithms.utils.stochastics.PriorityQueueStochasticSamplingStrategy;
import org.processmining.sccwbpmnnpos.algorithms.utils.stochastics.graph.MostProbableStochasticGraphPathSamplingStrategy;
import org.processmining.sccwbpmnnpos.algorithms.utils.stochastics.graph.RandomStochasticGraphPathSamplingStrategy;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.factory.BpmnMarkingFactory;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.factory.DefaultBpmnMarkingFactory;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.token.factory.BpmnTokenFactory;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.token.factory.SimpleBpmnTokenFactory;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.utils.BpmnMarkingUtils;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.utils.SimpleBpmnMarkingUtils;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.node.factory.CachedExecutableBpmnNodeFactory;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.node.factory.ExecutableBpmnNodeFactory;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.node.factory.SimpleExecutableBpmnNodeFactory;
import org.processmining.sccwbpmnnpos.models.bpmn.stochastic.execution.node.factory.ExecutableStochasticBpmnNodeFactoryImpl;
import org.processmining.sccwbpmnnpos.models.bpmn.stochastic.language.BpmnStochasticPathLanguage;
import org.processmining.sccwbpmnnpos.models.utils.multiset.factory.DefaultMultisetFactory;
import org.processmining.sccwbpmnnpos.models.utils.multiset.factory.MultisetFactory;
import org.processmining.sccwbpmnnpos.models.utils.multiset.utils.MultisetUtils;
import org.processmining.sccwbpmnnpos.models.utils.multiset.utils.SimpleMultisetUtils;
import org.processmining.stochasticbpmn.algorithms.diagram.builder.StochasticBPMNDiagramBuilderImpl;
import org.processmining.stochasticbpmn.algorithms.diagram.reader.StochasticBPMNDiagramReader;
import org.processmining.stochasticbpmn.algorithms.reader.ObjectFilePathReader;
import org.processmining.stochasticbpmn.algorithms.reader.ObjectReader;
import org.processmining.stochasticbpmn.algorithms.reader.StochasticBPMNInputStreamReader;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticBPMNDiagram;

public class ExampleRunner {
    public static void main(String[] args) {
        StochasticBpmnRG2StochasticLanguagePathConverterImpl toStochasticLanguage =
                new StochasticBpmnRG2StochasticLanguagePathConverterImpl(new RandomStochasticGraphPathSamplingStrategy<>());
        MultisetFactory multisetFactory = new DefaultMultisetFactory();
        MultisetUtils multisetUtils = new SimpleMultisetUtils(multisetFactory);
        BpmnTokenFactory tokenFactory = new SimpleBpmnTokenFactory();
        BpmnMarkingFactory markingFactory = new DefaultBpmnMarkingFactory(multisetFactory);
        BpmnMarkingUtils markingUtils = new SimpleBpmnMarkingUtils(multisetUtils, markingFactory);
        ExecutableBpmnNodeFactory executableNodeFactory =
                new CachedExecutableBpmnNodeFactory(new ExecutableStochasticBpmnNodeFactoryImpl(new SimpleExecutableBpmnNodeFactory(tokenFactory,
                        markingFactory, markingUtils), tokenFactory, markingFactory, markingUtils));

        CartesianProductCalculator cartesianProductCalculator = new NestedLoopsCartesianProductCalculator();
        StochasticBpmn2ReachabilityGraphConverter bpmn2StochasticLanguageConverter =
                new StochasticBpmn2PartiallyOrderedReachabilityGraphConverter(executableNodeFactory, markingFactory,
                        cartesianProductCalculator);
        ObjectReader<String, StochasticBPMNDiagram> diagramReader =
                new ObjectFilePathReader<>(new StochasticBPMNDiagramReader(new StochasticBPMNInputStreamReader(),
                        new StochasticBPMNDiagramBuilderImpl()));
        try {
            StochasticBPMNDiagram diagram = diagramReader.read("/home/aleks/Documents/Learn/Playground/obsidianTest" +
                    "/alkuzman/Research/Concepts/Process Management/Process Mining/Process " +
                    "Models/BPMN/Instances/Instance - BPMN - Handling of Compensation Requests.bpmn");
            ReachabilityGraph stateSpace = bpmn2StochasticLanguageConverter.convert(diagram);
            BpmnStochasticPathLanguage stochasticLanguage = toStochasticLanguage.convert(stateSpace);
            System.out.println(stochasticLanguage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

/*        final ObjectReader<String, XLog> logReader = new ObjectFilePathReader<>(new XLogReaderImpl());
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
        }*/
    }
}
