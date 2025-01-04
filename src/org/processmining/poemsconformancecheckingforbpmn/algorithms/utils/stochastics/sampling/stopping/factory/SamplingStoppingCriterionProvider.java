package org.processmining.poemsconformancecheckingforbpmn.algorithms.utils.stochastics.sampling.stopping.factory;

import org.processmining.poemsconformancecheckingforbpmn.algorithms.utils.stochastics.sampling.stopping.SamplingStoppingCriterion;
import org.processmining.stochasticbpmn.models.stochastic.Probability;

public interface SamplingStoppingCriterionProvider {
    SamplingStoppingCriterion get(Probability populationProbability);
}
