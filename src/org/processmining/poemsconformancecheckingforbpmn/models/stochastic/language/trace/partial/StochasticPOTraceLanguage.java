package org.processmining.poemsconformancecheckingforbpmn.models.stochastic.language.trace.partial;

import org.processmining.poemsconformancecheckingforbpmn.models.stochastic.language.StochasticLanguage;
import org.processmining.poemsconformancecheckingforbpmn.models.utils.activity.Activity;
import org.processmining.poemsconformancecheckingforbpmn.models.trace.partial_order.PartiallyOrderedTrace;

public interface StochasticPOTraceLanguage<A extends Activity, T extends PartiallyOrderedTrace<A>> extends StochasticLanguage<A, T> {
}
