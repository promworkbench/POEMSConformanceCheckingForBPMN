package org.processmining.sccwbpmnnpos.algorithms.utils.cartesianproduct;

import java.util.Collection;
import java.util.List;

public interface CartesianProductCalculator {
    <T> List<List<T>> calculate(Collection<? extends Collection<T>> vectors);
}
