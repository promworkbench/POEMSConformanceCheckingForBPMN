package org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.stochastic_language;

import org.deckfour.xes.model.XLog;
import org.processmining.models.graphbased.directed.transitionsystem.ReachabilityGraph;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.bpmn.statespace.BpmnNoOptionToCompleteException;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.bpmn.statespace.BpmnUnboundedException;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.bpmn.stochastic.statespace.StochasticBpmn2POReachabilityGraphConverter;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.reachability_graph.stochastic.language.path.SPORG2StochasticPathLanguageConverter;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.reachability_graph.stochastic.language.trace.SPORG2StochasticTraceLanguageConverter;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.log.stohastic_language.xlog.Xlog2StochasticTraceLanguageConverter;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.reachability_graph.stochastic.analyzer.StochasticReachabilityGraphStaticAnalysis;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.reachability_graph.stochastic.analyzer.StochasticReachabilityGraphStaticAnalyzer;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.marking.BpmnMarking;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.stochastic.language.path.BpmnStochasticPOPathLanguage;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.stochastic.language.trace.BpmnStochasticPOTraceLanguage;
import org.processmining.poemsconformancecheckingforbpmn.models.log.stochastic.language.EventLogStochasticTOTraceLanguage;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticBPMNDiagram;

public class StochasticLanguageGeneratorImpl implements StochasticLanguageGenerator {
    private final Xlog2StochasticTraceLanguageConverter log2Trace;
    private final StochasticBpmn2POReachabilityGraphConverter sbpmn2Graph;
    private final SPORG2StochasticPathLanguageConverter rg2POPath;
    private final SPORG2StochasticTraceLanguageConverter rg2POTrace;

    public StochasticLanguageGeneratorImpl(Xlog2StochasticTraceLanguageConverter log2Trace, StochasticBpmn2POReachabilityGraphConverter sbpmn2Graph, SPORG2StochasticPathLanguageConverter rg2POPath, SPORG2StochasticTraceLanguageConverter rg2POTrace) {
        this.log2Trace = log2Trace;
        this.sbpmn2Graph = sbpmn2Graph;
        this.rg2POPath = rg2POPath;
        this.rg2POTrace = rg2POTrace;
    }

    @Override
    public BpmnStochasticPOPathLanguage poPath(StochasticBPMNDiagram diagram) throws BpmnNoOptionToCompleteException, BpmnUnboundedException {
        ReachabilityGraph graph = sbpmn2Graph.convert(diagram);
        return poPath(graph);
    }

    @Override
    public BpmnStochasticPOTraceLanguage poTrace(StochasticBPMNDiagram diagram) throws BpmnNoOptionToCompleteException, BpmnUnboundedException {
        ReachabilityGraph graph = sbpmn2Graph.convert(diagram);
        return poTrace(graph);
    }

    @Override
    public BpmnStochasticPOPathLanguage poPath(ReachabilityGraph graph) {
        return rg2POPath.convert(graph);
    }

    @Override
    public BpmnStochasticPOTraceLanguage poTrace(ReachabilityGraph graph) {
        return rg2POTrace.convert(graph);
    }

    @Override
    public EventLogStochasticTOTraceLanguage trace(XLog log) {
        return log2Trace.convert(log);
    }
}
