package org.processmining.sccwbpmnnpos.algorithms.utils.stochastics;

import java.util.Iterator;

public interface SamplingStrategy<E> {
    Sampler<E> getSampler();
}
