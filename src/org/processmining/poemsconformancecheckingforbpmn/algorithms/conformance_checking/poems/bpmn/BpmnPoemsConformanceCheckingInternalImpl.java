package org.processmining.poemsconformancecheckingforbpmn.algorithms.conformance_checking.poems.bpmn;

import org.processmining.poemsconformancecheckingforbpmn.algorithms.conformance_checking.poems.POEMSConformanceChecking;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.bpmn.statespace.BpmnNoOptionToCompleteException;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.bpmn.statespace.BpmnUnboundedException;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.log.stohastic_language.simplified.SimplifiedLog2StochasticLanguageConverter;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.bpmn.stochastic.language.trace.StochasticBpmn2POStochasticTraceLanguageConverter;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.stochastic.language.trace.BpmnStochasticPOTraceLanguage;
import org.processmining.poemsconformancecheckingforbpmn.models.log.stochastic.language.EventLogStochasticTOTraceLanguage;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.conformance.result.POEMSConformanceCheckingResult;
import org.processmining.poemsconformancecheckingforbpmn.models.log.SimplifiedEventLog;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticBPMNDiagram;

public class BpmnPoemsConformanceCheckingInternalImpl implements BpmnPoemsConformanceCheckingInternal {
    private final POEMSConformanceChecking POEMSConformanceChecking;
    private final SimplifiedLog2StochasticLanguageConverter logToStochasticLanguageConverter;
    private final StochasticBpmn2POStochasticTraceLanguageConverter modelToStochasticLanguageConverter;

    public BpmnPoemsConformanceCheckingInternalImpl(POEMSConformanceChecking POEMSConformanceChecking, SimplifiedLog2StochasticLanguageConverter logToStochasticLanguageConverter, StochasticBpmn2POStochasticTraceLanguageConverter modelToStochasticLanguageConverter) {
        this.POEMSConformanceChecking = POEMSConformanceChecking;
        this.logToStochasticLanguageConverter = logToStochasticLanguageConverter;
        this.modelToStochasticLanguageConverter = modelToStochasticLanguageConverter;
    }

    @Override
    public POEMSConformanceCheckingResult calculateConformance(StochasticBPMNDiagram stochasticBPMNDiagram, SimplifiedEventLog simplifiedEventLogVariants) throws BpmnNoOptionToCompleteException, BpmnUnboundedException, InterruptedException {
        BpmnStochasticPOTraceLanguage sl1 = modelToStochasticLanguageConverter.convert(stochasticBPMNDiagram);
        EventLogStochasticTOTraceLanguage sl2 = logToStochasticLanguageConverter.convert(simplifiedEventLogVariants);
        return POEMSConformanceChecking.calculateConformance(sl1, sl2);
    }
}
