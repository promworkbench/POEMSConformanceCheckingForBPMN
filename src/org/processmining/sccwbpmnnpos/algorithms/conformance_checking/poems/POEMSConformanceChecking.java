package org.processmining.sccwbpmnnpos.algorithms.conformance_checking.poems;

import org.processmining.sccwbpmnnpos.models.stochastic.language.trace.partial.StochasticPOTraceLanguage;
import org.processmining.sccwbpmnnpos.models.stochastic.language.trace.total.StochasticTOTraceLanguage;
import org.processmining.sccwbpmnnpos.models.utils.activity.Activity;
import org.processmining.sccwbpmnnpos.models.trace.partial_order.PartiallyOrderedTrace;
import org.processmining.sccwbpmnnpos.models.trace.total_order.TotallyOrderedTrace;
import org.processmining.sccwbpmnnpos.models.bpmn.conformance.result.POEMSConformanceCheckingResult;

public interface POEMSConformanceChecking {
    <A extends Activity, PO extends PartiallyOrderedTrace<A>, TO extends TotallyOrderedTrace<A>> POEMSConformanceCheckingResult calculateConformance(final StochasticPOTraceLanguage<A, PO> poStochasticLanguage, final StochasticTOTraceLanguage<A, TO> stochasticLanguage) throws InterruptedException;
}
