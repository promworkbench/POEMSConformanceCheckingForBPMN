package org.processmining.poemsconformancecheckingforbpmn.algorithms.conformance_checking.poems.bpmn;

import org.deckfour.xes.model.XLog;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.conformance_checking.poems.POEMSConformanceChecking;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.bpmn.statespace.BpmnNoOptionToCompleteException;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.bpmn.statespace.BpmnUnboundedException;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.stochastic_language.StochasticLanguageGenerator;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.conformance.result.POEMSConformanceCheckingResult;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.stochastic.language.trace.BpmnStochasticPOTraceLanguage;
import org.processmining.poemsconformancecheckingforbpmn.models.log.stochastic.language.EventLogStochasticTOTraceLanguage;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticBPMNDiagram;

public class BpmnPoemsConformanceCheckingImpl implements BpmnPoemsConformanceChecking {
    private final StochasticLanguageGenerator stochasticLanguageGenerator;
    private final POEMSConformanceChecking conformanceChecker;

    public BpmnPoemsConformanceCheckingImpl(
            StochasticLanguageGenerator stochasticLanguageGenerator,
            POEMSConformanceChecking conformanceChecker
    ) {
        this.stochasticLanguageGenerator = stochasticLanguageGenerator;
        this.conformanceChecker = conformanceChecker;
    }

    @Override
    public POEMSConformanceCheckingResult calculateConformance(
            StochasticBPMNDiagram model,
            XLog log
    ) throws BpmnNoOptionToCompleteException, BpmnUnboundedException, InterruptedException {
        EventLogStochasticTOTraceLanguage logLanguage = stochasticLanguageGenerator.trace(log);
        BpmnStochasticPOTraceLanguage modelLanguage = stochasticLanguageGenerator.poTrace(model);
        return conformanceChecker.calculateConformance(
                modelLanguage,
                logLanguage
        );
    }
}
