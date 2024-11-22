package org.processmining.sccwbpmnnpos.algorithms.inputs.log.stohastic_language;

import org.processmining.sccwbpmnnpos.models.utils.activity.Activity;
import org.processmining.sccwbpmnnpos.models.utils.trace.total_order.TotallyOrderedTrace;
import org.processmining.sccwbpmnnpos.models.log.SimplifiedEventLog;
import org.processmining.sccwbpmnnpos.models.stochastic_language.StochasticLanguage;

public interface SimplifiedLogToStochasticLanguageConverter {
    StochasticLanguage<Activity, TotallyOrderedTrace> convert(final SimplifiedEventLog eventLog);
}
