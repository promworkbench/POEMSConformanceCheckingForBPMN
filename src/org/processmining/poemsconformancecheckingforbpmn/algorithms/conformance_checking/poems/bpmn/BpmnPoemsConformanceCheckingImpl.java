package org.processmining.poemsconformancecheckingforbpmn.algorithms.conformance_checking.poems.bpmn;

import org.deckfour.xes.model.XLog;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.bpmn.statespace.BpmnNoOptionToCompleteException;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.bpmn.statespace.BpmnUnboundedException;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.log.simplifier.XLogSimplifier;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.conformance.result.POEMSConformanceCheckingResult;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticBPMNDiagram;

public class BpmnPoemsConformanceCheckingImpl implements BpmnPoemsConformanceChecking {
    private final XLogSimplifier logSimplifier;
    private final BpmnPoemsConformanceCheckingInternal conformanceCheckingMethod;

    public BpmnPoemsConformanceCheckingImpl(XLogSimplifier logSimplifier, BpmnPoemsConformanceCheckingInternal conformanceCheckingMethod) {
        this.logSimplifier = logSimplifier;
        this.conformanceCheckingMethod = conformanceCheckingMethod;
    }

    @Override
    public POEMSConformanceCheckingResult calculateConformance(StochasticBPMNDiagram model, XLog log) throws BpmnNoOptionToCompleteException, BpmnUnboundedException, InterruptedException {
        return conformanceCheckingMethod.calculateConformance(model, logSimplifier.simplify(log));
    }
}
