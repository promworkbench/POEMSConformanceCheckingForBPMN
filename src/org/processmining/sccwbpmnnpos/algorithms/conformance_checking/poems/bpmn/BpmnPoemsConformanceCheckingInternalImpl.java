package org.processmining.sccwbpmnnpos.algorithms.conformance_checking.poems.bpmn;

import org.processmining.models.graphbased.directed.bpmn.BPMNNode;
import org.processmining.sccwbpmnnpos.algorithms.conformance_checking.poems.PoemsConformanceChecking;
import org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.statespace.BpmnNoOptionToCompleteException;
import org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.statespace.BpmnUnboundedException;
import org.processmining.sccwbpmnnpos.algorithms.inputs.log.stohastic_language.SimplifiedLogToStochasticLanguageConverter;
import org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.stochastic.language.StochasticBpmn2StochasticLanguageConverter;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.path.BpmnPartiallyOrderedPath;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.trace.BpmnModelTrace;
import org.processmining.sccwbpmnnpos.models.log.SimplifiedEventLogTrace;
import org.processmining.sccwbpmnnpos.models.utils.activity.Activity;
import org.processmining.sccwbpmnnpos.models.utils.trace.partial_order.PartiallyOrderedTrace;
import org.processmining.sccwbpmnnpos.models.utils.trace.total_order.TotallyOrderedTrace;
import org.processmining.sccwbpmnnpos.models.bpmn.conformance.result.ConformanceCheckingResult;
import org.processmining.sccwbpmnnpos.models.log.SimplifiedEventLog;
import org.processmining.sccwbpmnnpos.models.stochastic.language.StochasticLanguage;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticBPMNDiagram;

public class BpmnPoemsConformanceCheckingInternalImpl implements BpmnPoemsConformanceCheckingInternal {
    private final PoemsConformanceChecking poemsConformanceChecking;
    private final SimplifiedLogToStochasticLanguageConverter logToStochasticLanguageConverter;
    private final StochasticBpmn2StochasticLanguageConverter modelToStochasticLanguageConverter;

    public BpmnPoemsConformanceCheckingInternalImpl(PoemsConformanceChecking poemsConformanceChecking, SimplifiedLogToStochasticLanguageConverter logToStochasticLanguageConverter, StochasticBpmn2StochasticLanguageConverter modelToStochasticLanguageConverter) {
        this.poemsConformanceChecking = poemsConformanceChecking;
        this.logToStochasticLanguageConverter = logToStochasticLanguageConverter;
        this.modelToStochasticLanguageConverter = modelToStochasticLanguageConverter;
    }

    @Override
    public ConformanceCheckingResult calculateConformance(StochasticBPMNDiagram stochasticBPMNDiagram, SimplifiedEventLog simplifiedEventLogVariants) throws BpmnNoOptionToCompleteException, BpmnUnboundedException {
        final StochasticLanguage<Activity, BpmnModelTrace> sl1 = modelToStochasticLanguageConverter.convert(stochasticBPMNDiagram);
        final StochasticLanguage<Activity, SimplifiedEventLogTrace> sl2 = logToStochasticLanguageConverter.convert(simplifiedEventLogVariants);
        return poemsConformanceChecking.calculateConformance(sl1, sl2);
    }
}
