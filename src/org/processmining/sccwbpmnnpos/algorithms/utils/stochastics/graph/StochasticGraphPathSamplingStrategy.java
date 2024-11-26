package org.processmining.sccwbpmnnpos.algorithms.utils.stochastics.graph;

import org.processmining.sccwbpmnnpos.algorithms.utils.stochastics.StochasticSamplingStrategy;
import org.processmining.sccwbpmnnpos.algorithms.utils.stochastics.graph.StochasticGraphPathSamplingStrategy.PathOption;
import org.processmining.stochasticbpmn.models.stochastic.Probability;
import org.processmining.stochasticbpmn.models.stochastic.StochasticObject;

public interface StochasticGraphPathSamplingStrategy<T extends PathOption<? extends StochasticObject, ? extends StochasticObject>> extends StochasticSamplingStrategy<T> {

    interface PathOption<S extends StochasticObject, P extends StochasticObject> extends StochasticObject {
        S getState();

        P getPathOption();

        default Probability getProbability() {
            return getState().getProbability().multiply(getPathOption().getProbability());
        }
    }
}
