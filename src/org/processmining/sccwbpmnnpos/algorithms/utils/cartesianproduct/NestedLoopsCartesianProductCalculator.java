package org.processmining.sccwbpmnnpos.algorithms.utils.cartesianproduct;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class NestedLoopsCartesianProductCalculator implements CartesianProductCalculator {
    @Override
    public <T> List<List<T>> calculate(Collection<? extends Collection<T>> vectors) {
        List<List<T>> result = new LinkedList<>();
        if (Objects.isNull(vectors) || vectors.isEmpty()) {
            return result;
        }

        // Start with an empty list
        result.add(new LinkedList<>());

        for (Collection<T> vector : vectors) {
            List<List<T>> temp = new LinkedList<>();
            for (List<T> combination : result) {
                for (T element : vector) {
                    List<T> newCombination = new LinkedList<>(combination);
                    newCombination.add(element);
                    temp.add(newCombination);
                }
            }
            result = temp;
        }
        return result;
    }
}
