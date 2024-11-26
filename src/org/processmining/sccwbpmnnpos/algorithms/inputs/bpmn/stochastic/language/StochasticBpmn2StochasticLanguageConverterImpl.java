package org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.stochastic.language;

import org.processmining.models.graphbased.directed.transitionsystem.TransitionSystem;
import org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.statespace.BpmnNoOptionToCompleteException;
import org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.statespace.BpmnUnboundedException;
import org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.stochastic.statespace.StochasticBpmn2ReachabilityGraphConverter;
import org.processmining.sccwbpmnnpos.models.stochastic.language.StochasticLanguage;
import org.processmining.sccwbpmnnpos.models.utils.activity.Activity;
import org.processmining.sccwbpmnnpos.models.utils.trace.partial_order.PartiallyOrderedTrace;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticBPMNDiagram;

public class StochasticBpmn2StochasticLanguageConverterImpl implements StochasticBpmn2StochasticLanguageConverter {
    private final StochasticBpmn2ReachabilityGraphConverter toReachabilityGraphConverter;
    ;

    public StochasticBpmn2StochasticLanguageConverterImpl(StochasticBpmn2ReachabilityGraphConverter toReachabilityGraphConverter) {
        this.toReachabilityGraphConverter = toReachabilityGraphConverter;
    }

    @Override
    public StochasticLanguage<Activity, PartiallyOrderedTrace> convert(StochasticBPMNDiagram bpmnDiagram) throws BpmnNoOptionToCompleteException, BpmnUnboundedException {
        TransitionSystem rg = toReachabilityGraphConverter.convert(bpmnDiagram);
        return null;
    }
}
