package org.processmining.poemsconformancecheckingforbpmn.parameters;

import org.processmining.poemsconformancecheckingforbpmn.algorithms.utils.stochastics.sampling.stopping.SamplingStoppingCriterionType;

import java.util.Map;

public interface SamplingStoppingCriteriaParameters {
    Map<SamplingStoppingCriterionType, Object> getSamplingStoppingCriteriaParameters();
}
