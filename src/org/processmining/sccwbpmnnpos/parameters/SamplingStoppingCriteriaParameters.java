package org.processmining.sccwbpmnnpos.parameters;

import org.processmining.sccwbpmnnpos.algorithms.utils.stochastics.sampling.stopping.SamplingStoppingCriterionType;

import java.util.Map;

public interface SamplingStoppingCriteriaParameters {
    Map<SamplingStoppingCriterionType, Object> getSamplingStoppingCriteriaParameters();
}
