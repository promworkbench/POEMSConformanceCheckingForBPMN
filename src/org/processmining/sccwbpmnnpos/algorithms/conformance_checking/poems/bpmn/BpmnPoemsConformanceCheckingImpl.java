package org.processmining.sccwbpmnnpos.algorithms.conformance_checking.poems.bpmn;

import org.deckfour.xes.model.XLog;
import org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.statespace.BpmnNoOptionToCompleteException;
import org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.statespace.BpmnUnboundedException;
import org.processmining.sccwbpmnnpos.algorithms.inputs.log.simplifier.XLogSimplifier;
import org.processmining.sccwbpmnnpos.models.bpmn.conformance.result.ConformanceCheckingResult;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticBPMNDiagram;

public class BpmnPoemsConformanceCheckingImpl implements BpmnPoemsConformanceChecking {
    private final XLogSimplifier logSimplifier;
    private final BpmnPoemsConformanceCheckingInternal conformanceCheckingMethod;

    public BpmnPoemsConformanceCheckingImpl(XLogSimplifier logSimplifier, BpmnPoemsConformanceCheckingInternal conformanceCheckingMethod) {
        this.logSimplifier = logSimplifier;
        this.conformanceCheckingMethod = conformanceCheckingMethod;
    }

    @Override
    public ConformanceCheckingResult calculateConformance(StochasticBPMNDiagram model, XLog log) throws BpmnNoOptionToCompleteException, BpmnUnboundedException {
        return conformanceCheckingMethod.calculateConformance(model, logSimplifier.simplify(log));
    }
}
