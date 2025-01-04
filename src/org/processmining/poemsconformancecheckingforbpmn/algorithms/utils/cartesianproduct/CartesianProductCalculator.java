package org.processmining.poemsconformancecheckingforbpmn.algorithms.utils.cartesianproduct;

import java.util.Collection;
import java.util.List;

public interface CartesianProductCalculator {
    static CartesianProductCalculator getInstance() {
        return new NestedLoopsCartesianProductCalculator();
    }

    <T> List<List<T>> calculate(Collection<? extends Collection<T>> vectors);
}
