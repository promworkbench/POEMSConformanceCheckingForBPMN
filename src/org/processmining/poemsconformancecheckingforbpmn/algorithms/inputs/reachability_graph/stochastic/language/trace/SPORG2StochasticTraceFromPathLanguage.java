package org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.reachability_graph.stochastic.language.trace;

import org.processmining.models.graphbased.directed.bpmn.BPMNNode;
import org.processmining.models.graphbased.directed.transitionsystem.ReachabilityGraph;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.bpmn.execution.BpmnPOPath2TraceConverter;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.reachability_graph.stochastic.language.path.SPORG2StochasticPathLanguageConverter;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.path.BpmnPartiallyOrderedPath;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.trace.BpmnPartiallyOrderedTrace;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.stochastic.language.path.BpmnStochasticPOPathLanguage;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.stochastic.language.trace.BpmnStochasticPOTraceLanguage;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.stochastic.language.trace.LinkedListBpmnStochasticPOPathLanguage;
import org.processmining.poemsconformancecheckingforbpmn.models.stochastic.language.StochasticLanguageEntry;

public class SPORG2StochasticTraceFromPathLanguage implements SPORG2StochasticTraceLanguageConverter {
    private final SPORG2StochasticPathLanguageConverter pathGenerator;
    private final BpmnPOPath2TraceConverter path2TraceConverter;

    public SPORG2StochasticTraceFromPathLanguage(SPORG2StochasticPathLanguageConverter pathGenerator, BpmnPOPath2TraceConverter path2TraceConverter) {
        this.pathGenerator = pathGenerator;
        this.path2TraceConverter = path2TraceConverter;
    }

    @Override
    public BpmnStochasticPOTraceLanguage convert(ReachabilityGraph transitionSystem) {
        BpmnStochasticPOPathLanguage pathLanguage = pathGenerator.convert(transitionSystem);
        LinkedListBpmnStochasticPOPathLanguage traceLanguage = new LinkedListBpmnStochasticPOPathLanguage();
        for (StochasticLanguageEntry<BPMNNode, BpmnPartiallyOrderedPath> entry : pathLanguage) {
            BpmnPartiallyOrderedTrace trace = path2TraceConverter.convert(entry.getElement());
            traceLanguage.add(trace, entry.getProbability());
        }
        return traceLanguage;
    }
}
