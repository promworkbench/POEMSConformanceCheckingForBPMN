package org.processmining.poemsconformancecheckingforbpmn.algorithms.conformance_checking;

import org.deckfour.xes.classification.XEventNameClassifier;
import org.deckfour.xes.model.XLog;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.conformance_checking.poems.bpmn.BpmnPoemsConformanceChecking;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.utils.stochastics.sampling.stopping.factory.SamplingStoppingCriterionProvider;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.utils.stochastics.sampling.strategy.transition.TransitionSamplingStrategy;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.conformance.result.ConformanceCheckingResult;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.conformance.result.POEMSConformanceCheckingResult;
import org.processmining.stochasticbpmn.models.bpmn.stochastic.StochasticBpmn;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticBPMNDiagram;

public interface BPMNStochasticConformanceChecking<O extends ConformanceCheckingResult> extends ConformanceChecking<StochasticBPMNDiagram, XLog, O> {

    /**
     * Create a POEMS (Partially Ordered Earth Movers') conformance checking service for BPMN.
     * For more configuration parameters see {@link BpmnPoemsConformanceChecking}.
     * @return BpmnPoemsConformanceChecking
     */
    static BpmnPoemsConformanceChecking poems() {
        return BpmnPoemsConformanceChecking.getInstance();
    }
}
