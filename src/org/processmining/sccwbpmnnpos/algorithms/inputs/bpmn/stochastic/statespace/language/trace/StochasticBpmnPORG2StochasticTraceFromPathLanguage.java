package org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.stochastic.statespace.language.trace;

import org.processmining.models.graphbased.directed.bpmn.BPMNNode;
import org.processmining.models.graphbased.directed.transitionsystem.ReachabilityGraph;
import org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.execution.BpmnPOPath2TraceConverter;
import org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.stochastic.statespace.language.path.StochasticBpmnPORG2StochasticPathLanguageConverter;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.path.BpmnPartiallyOrderedPath;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.trace.BpmnPartiallyOrderedTrace;
import org.processmining.sccwbpmnnpos.models.bpmn.stochastic.language.path.BpmnStochasticPOPathLanguage;
import org.processmining.sccwbpmnnpos.models.bpmn.stochastic.language.trace.BpmnStochasticPOTraceLanguage;
import org.processmining.sccwbpmnnpos.models.bpmn.stochastic.language.trace.LinkedListBpmnStochasticPOPathLanguage;
import org.processmining.sccwbpmnnpos.models.stochastic.language.StochasticLanguageEntry;

public class StochasticBpmnPORG2StochasticTraceFromPathLanguage implements StochasticBpmnPORG2StochasticTraceLanguageConverter {
    private final StochasticBpmnPORG2StochasticPathLanguageConverter pathGenerator;
    private final BpmnPOPath2TraceConverter path2TraceConverter;

    public StochasticBpmnPORG2StochasticTraceFromPathLanguage(StochasticBpmnPORG2StochasticPathLanguageConverter pathGenerator, BpmnPOPath2TraceConverter path2TraceConverter) {
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
