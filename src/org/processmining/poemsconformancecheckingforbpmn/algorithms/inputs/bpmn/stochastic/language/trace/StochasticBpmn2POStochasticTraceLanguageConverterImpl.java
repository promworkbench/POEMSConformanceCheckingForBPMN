package org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.bpmn.stochastic.language.trace;

import org.processmining.models.graphbased.directed.transitionsystem.ReachabilityGraph;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.bpmn.statespace.BpmnNoOptionToCompleteException;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.bpmn.statespace.BpmnUnboundedException;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.bpmn.stochastic.statespace.StochasticBpmn2POReachabilityGraphConverter;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.bpmn.stochastic.statespace.language.trace.StochasticBpmnPORG2StochasticTraceLanguageConverter;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.stochastic.language.trace.BpmnStochasticPOTraceLanguage;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticBPMNDiagram;

public class StochasticBpmn2POStochasticTraceLanguageConverterImpl implements StochasticBpmn2POStochasticTraceLanguageConverter {
    private final StochasticBpmn2POReachabilityGraphConverter sbpmn2RgConverter;
    private final StochasticBpmnPORG2StochasticTraceLanguageConverter rg2TraceLanguageConverter;

    public StochasticBpmn2POStochasticTraceLanguageConverterImpl(StochasticBpmn2POReachabilityGraphConverter sbpmn2RgConverter
            , StochasticBpmnPORG2StochasticTraceLanguageConverter rg2TraceLanguageConverter) {
        this.sbpmn2RgConverter = sbpmn2RgConverter;
        this.rg2TraceLanguageConverter = rg2TraceLanguageConverter;
    }

    @Override
    public BpmnStochasticPOTraceLanguage convert(StochasticBPMNDiagram bpmnDiagram) throws BpmnNoOptionToCompleteException, BpmnUnboundedException {
        ReachabilityGraph rg = sbpmn2RgConverter.convert(bpmnDiagram);
        return rg2TraceLanguageConverter.convert(rg);
    }
}
