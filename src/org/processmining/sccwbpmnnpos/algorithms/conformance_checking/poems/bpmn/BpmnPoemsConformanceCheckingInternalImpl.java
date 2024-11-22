package org.processmining.sccwbpmnnpos.algorithms.conformance_checking.poems.bpmn;

import org.processmining.sccwbpmnnpos.algorithms.conformance_checking.poems.PoemsConformanceChecking;
import org.processmining.sccwbpmnnpos.algorithms.inputs.log.stohastic_language.SimplifiedLogToStochasticLanguageConverter;
import org.processmining.sccwbpmnnpos.algorithms.inputs.model.StochasticBpmnToStochasticLanguageConverter;
import org.processmining.sccwbpmnnpos.models.utils.activity.Activity;
import org.processmining.sccwbpmnnpos.models.utils.trace.partial_order.PartiallyOrderedTrace;
import org.processmining.sccwbpmnnpos.models.utils.trace.total_order.TotallyOrderedTrace;
import org.processmining.sccwbpmnnpos.models.conformance.result.ConformanceCheckingResult;
import org.processmining.sccwbpmnnpos.models.log.SimplifiedEventLog;
import org.processmining.sccwbpmnnpos.models.stochastic_language.StochasticLanguage;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticBPMNDiagram;

public class BpmnPoemsConformanceCheckingInternalImpl implements BpmnPoemsConformanceCheckingInternal {
    private final PoemsConformanceChecking poemsConformanceChecking;
    private final SimplifiedLogToStochasticLanguageConverter logToStochasticLanguageConverter;
    private final StochasticBpmnToStochasticLanguageConverter modelToStochasticLanguageConverter;

    public BpmnPoemsConformanceCheckingInternalImpl(PoemsConformanceChecking poemsConformanceChecking, SimplifiedLogToStochasticLanguageConverter logToStochasticLanguageConverter, StochasticBpmnToStochasticLanguageConverter modelToStochasticLanguageConverter) {
        this.poemsConformanceChecking = poemsConformanceChecking;
        this.logToStochasticLanguageConverter = logToStochasticLanguageConverter;
        this.modelToStochasticLanguageConverter = modelToStochasticLanguageConverter;
    }

    @Override
    public ConformanceCheckingResult calculateConformance(StochasticBPMNDiagram stochasticBPMNDiagram, SimplifiedEventLog simplifiedEventLogVariants) {
        final StochasticLanguage<Activity, PartiallyOrderedTrace> sl1 = modelToStochasticLanguageConverter.convert(stochasticBPMNDiagram);
        final StochasticLanguage<Activity, TotallyOrderedTrace> sl2 = logToStochasticLanguageConverter.convert(simplifiedEventLogVariants);
        return poemsConformanceChecking.calculateConformance(sl1, sl2);
    }
}
