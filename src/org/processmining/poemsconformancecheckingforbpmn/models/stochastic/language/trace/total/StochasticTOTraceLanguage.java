package org.processmining.poemsconformancecheckingforbpmn.models.stochastic.language.trace.total;

import org.processmining.poemsconformancecheckingforbpmn.models.stochastic.language.StochasticLanguage;
import org.processmining.poemsconformancecheckingforbpmn.models.utils.activity.Activity;
import org.processmining.poemsconformancecheckingforbpmn.models.trace.total_order.TotallyOrderedTrace;

public interface StochasticTOTraceLanguage<A extends Activity, T extends TotallyOrderedTrace<A>> extends StochasticLanguage<A, T> {
}
