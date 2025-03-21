package org.processmining.poemsconformancecheckingforbpmn.algorithms.conformance_checking.poems;

import org.processmining.poemsconformancecheckingforbpmn.models.stochastic.language.trace.partial.StochasticPOTraceLanguage;
import org.processmining.poemsconformancecheckingforbpmn.models.stochastic.language.trace.total.StochasticTOTraceLanguage;
import org.processmining.poemsconformancecheckingforbpmn.models.utils.activity.Activity;
import org.processmining.poemsconformancecheckingforbpmn.models.trace.partial_order.PartiallyOrderedTrace;
import org.processmining.poemsconformancecheckingforbpmn.models.trace.total_order.TotallyOrderedTrace;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.conformance.result.POEMSConformanceCheckingResult;

public interface POEMSConformanceChecking {
    <A extends Activity, PO extends PartiallyOrderedTrace<A>, TO extends TotallyOrderedTrace<A>> POEMSConformanceCheckingResult calculateConformance(final StochasticPOTraceLanguage<A, PO> poStochasticLanguage, final StochasticTOTraceLanguage<A, TO> stochasticLanguage) throws InterruptedException;
}
