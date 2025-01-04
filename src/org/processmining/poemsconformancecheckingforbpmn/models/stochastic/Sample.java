package org.processmining.poemsconformancecheckingforbpmn.models.stochastic;

import org.processmining.stochasticbpmn.models.stochastic.StochasticObject;

public interface Sample<T> extends Iterable<T>, StochasticObject {
    int size();
}
