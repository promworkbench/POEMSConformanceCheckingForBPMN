package org.processmining.sccwbpmnnpos.algorithms.inputs.stochastic_language;

import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.classification.XEventNameClassifier;
import org.deckfour.xes.model.XLog;
import org.processmining.models.graphbased.directed.transitionsystem.ReachabilityGraph;
import org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.statespace.BpmnNoOptionToCompleteException;
import org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.statespace.BpmnUnboundedException;
import org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.stochastic.statespace.StochasticBpmn2POReachabilityGraphConverter;
import org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.stochastic.statespace.language.path.StochasticBpmnPORG2StochasticPathLanguageConverter;
import org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.stochastic.statespace.language.trace.StochasticBpmnPORG2StochasticTraceLanguageConverter;
import org.processmining.sccwbpmnnpos.algorithms.inputs.log.stohastic_language.xlog.Xlog2StochasticTraceLanguageConverter;
import org.processmining.sccwbpmnnpos.algorithms.inputs.reachability_graph.stochastic.StochasticReachabilityGraphStaticAnalyzer;
import org.processmining.sccwbpmnnpos.algorithms.inputs.stochastic_language.stopping.StochasticLanguageGeneratorStopper;
import org.processmining.sccwbpmnnpos.algorithms.utils.stochastics.sampling.strategy.graph.StochasticGraphPathSamplingStrategy;
import org.processmining.sccwbpmnnpos.algorithms.utils.stochastics.sampling.strategy.graph.StochasticGraphPathSamplingStrategy.GraphSamplingType;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.BpmnMarking;
import org.processmining.sccwbpmnnpos.models.bpmn.stochastic.execution.node.factory.ExecutableStochasticBpmnNodeFactory;
import org.processmining.sccwbpmnnpos.models.bpmn.stochastic.language.path.BpmnStochasticPOPathLanguage;
import org.processmining.sccwbpmnnpos.models.bpmn.stochastic.language.trace.BpmnStochasticPOTraceLanguage;
import org.processmining.sccwbpmnnpos.models.log.stochastic.language.EventLogStochasticTOTraceLanguage;
import org.processmining.sccwbpmnnpos.models.utils.activity.factory.ActivityFactory;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticBPMNDiagram;

public interface StochasticLanguageGenerator {
    static StochasticLanguageGenerator getInstance(ActivityFactory activityFactory) {
        return getInstance(activityFactory, new XEventNameClassifier(), StochasticGraphPathSamplingStrategy.getDefaultType(), StochasticLanguageGeneratorStopper.getInstance());
    }

    static StochasticLanguageGenerator getInstance(ActivityFactory activityFactory, XEventClassifier defaultClassifier, GraphSamplingType samplingStrategy, StochasticLanguageGeneratorStopper stopper) {
        ExecutableStochasticBpmnNodeFactory nodeFactory = ExecutableStochasticBpmnNodeFactory.getInstance();
        StochasticBpmn2POReachabilityGraphConverter sbpmn2Graph =
                StochasticBpmn2POReachabilityGraphConverter.getInstance(nodeFactory);

        Xlog2StochasticTraceLanguageConverter log2Trace =
                Xlog2StochasticTraceLanguageConverter.getInstance(defaultClassifier, activityFactory);
        StochasticBpmnPORG2StochasticPathLanguageConverter graph2POPath =
                StochasticBpmnPORG2StochasticPathLanguageConverter.getInstance(samplingStrategy, stopper, nodeFactory);
        StochasticBpmnPORG2StochasticTraceLanguageConverter graph2POTrace =
                StochasticBpmnPORG2StochasticTraceLanguageConverter.getInstance(activityFactory, samplingStrategy, stopper);

        return new StochasticLanguageGeneratorImpl(log2Trace, sbpmn2Graph, graph2POPath, graph2POTrace,
                StochasticReachabilityGraphStaticAnalyzer.getInstance(BpmnMarking.class));
    }

    static StochasticLanguageGenerator getInstance(XEventClassifier defaultClassifier, GraphSamplingType samplingStrategy, StochasticLanguageGeneratorStopper stopper) {
        return getInstance(ActivityFactory.getInstance(), defaultClassifier, samplingStrategy, stopper);
    }

    static StochasticLanguageGenerator getInstance(GraphSamplingType samplingStrategy, StochasticLanguageGeneratorStopper stopper) {
        return getInstance(new XEventNameClassifier(), samplingStrategy, stopper);
    }

    static StochasticLanguageGenerator getInstance() {
        return getInstance(StochasticGraphPathSamplingStrategy.getDefaultType(), StochasticLanguageGeneratorStopper.getInstance());
    }

    BpmnStochasticPOPathLanguage poPath(final StochasticBPMNDiagram diagram) throws BpmnNoOptionToCompleteException,
            BpmnUnboundedException;

    BpmnStochasticPOTraceLanguage poTrace(final StochasticBPMNDiagram diagram) throws BpmnNoOptionToCompleteException
            , BpmnUnboundedException;

    BpmnStochasticPOPathLanguage poPath(final ReachabilityGraph graph);

    BpmnStochasticPOTraceLanguage poTrace(final ReachabilityGraph graph);

    EventLogStochasticTOTraceLanguage trace(final XLog log);
}
