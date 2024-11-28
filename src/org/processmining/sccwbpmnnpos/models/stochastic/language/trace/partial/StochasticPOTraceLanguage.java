package org.processmining.sccwbpmnnpos.models.stochastic.language.trace.partial;

import org.processmining.sccwbpmnnpos.models.stochastic.language.StochasticLanguage;
import org.processmining.sccwbpmnnpos.models.utils.activity.Activity;
import org.processmining.sccwbpmnnpos.models.trace.partial_order.PartiallyOrderedTrace;

public interface StochasticPOTraceLanguage<A extends Activity, T extends PartiallyOrderedTrace<A>> extends StochasticLanguage<A, T> {
}
