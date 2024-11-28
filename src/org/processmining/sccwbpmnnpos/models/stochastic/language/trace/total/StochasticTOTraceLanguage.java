package org.processmining.sccwbpmnnpos.models.stochastic.language.trace.total;

import org.processmining.sccwbpmnnpos.models.stochastic.language.StochasticLanguage;
import org.processmining.sccwbpmnnpos.models.utils.activity.Activity;
import org.processmining.sccwbpmnnpos.models.trace.total_order.TotallyOrderedTrace;

public interface StochasticTOTraceLanguage<A extends Activity, T extends TotallyOrderedTrace<A>> extends StochasticLanguage<A, T> {
}
