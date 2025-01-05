package org.processmining.poemsconformancecheckingforbpmn.parameters;

import org.processmining.poemsconformancecheckingforbpmn.algorithms.utils.stochastics.sampling.strategy.transition.TansitionSamplingStrategyType;

public interface SamplingStrategyParameters {
    TansitionSamplingStrategyType getSamplingStrategyType();
}
