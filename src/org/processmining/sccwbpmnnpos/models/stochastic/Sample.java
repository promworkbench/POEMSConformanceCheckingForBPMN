package org.processmining.sccwbpmnnpos.models.stochastic;

import org.processmining.sccwbpmnnpos.models.stochastic.language.StochasticLanguageEntry;
import org.processmining.stochasticbpmn.models.stochastic.StochasticObject;

public interface Sample<T> extends Iterable<T>, StochasticObject {
    int size();
}
