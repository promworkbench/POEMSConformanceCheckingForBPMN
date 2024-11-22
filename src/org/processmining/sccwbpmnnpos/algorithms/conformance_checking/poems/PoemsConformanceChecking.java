package org.processmining.sccwbpmnnpos.algorithms.conformance_checking.poems;

import org.processmining.sccwbpmnnpos.models.utils.activity.Activity;
import org.processmining.sccwbpmnnpos.models.utils.trace.partial_order.PartiallyOrderedTrace;
import org.processmining.sccwbpmnnpos.models.utils.trace.total_order.TotallyOrderedTrace;
import org.processmining.sccwbpmnnpos.models.conformance.result.ConformanceCheckingResult;
import org.processmining.sccwbpmnnpos.models.stochastic_language.StochasticLanguage;

public interface PoemsConformanceChecking {
    ConformanceCheckingResult calculateConformance(final StochasticLanguage<Activity, PartiallyOrderedTrace> poStochasticLanguage, final StochasticLanguage<Activity, TotallyOrderedTrace> stochasticLanguage);
}
