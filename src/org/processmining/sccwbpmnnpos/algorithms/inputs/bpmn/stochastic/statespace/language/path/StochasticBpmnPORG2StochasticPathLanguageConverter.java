package org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.stochastic.statespace.language.path;

import org.processmining.models.graphbased.directed.transitionsystem.ReachabilityGraph;
import org.processmining.sccwbpmnnpos.algorithms.utils.stochastics.graph.StochasticGraphPathSamplingStrategy;
import org.processmining.sccwbpmnnpos.algorithms.utils.stochastics.graph.StochasticGraphPathSamplingStrategy.Type;
import org.processmining.sccwbpmnnpos.models.bpmn.stochastic.execution.node.factory.ExecutableStochasticBpmnNodeFactory;
import org.processmining.sccwbpmnnpos.models.bpmn.stochastic.execution.node.factory.ExecutableStochasticBpmnNodeFactoryImpl;
import org.processmining.sccwbpmnnpos.models.bpmn.stochastic.language.path.BpmnStochasticPOPathLanguage;

public interface StochasticBpmnPORG2StochasticPathLanguageConverter {
    static StochasticBpmnPORG2StochasticPathLanguageConverter getInstance(Type type) {
        return getInstance(type, ExecutableStochasticBpmnNodeFactory.getInstance());
    }

    static StochasticBpmnPORG2StochasticPathLanguageConverter getInstance(Type type, ExecutableStochasticBpmnNodeFactory nodeFactory) {
        return new StochasticBpmnPORG2StochasticPathLanguageConverterImpl(StochasticGraphPathSamplingStrategy.getInstance(type), nodeFactory);
    }

    static StochasticBpmnPORG2StochasticPathLanguageConverter getInstance() {
        return getInstance(StochasticGraphPathSamplingStrategy.getDefaultType());
    }

    BpmnStochasticPOPathLanguage convert(ReachabilityGraph transitionSystem);
}
