package org.processmining.sccwbpmnnpos.algorithms.utils.cartesianproduct;

import java.util.*;

public class NestedLoopsCartesianProductCalculator implements CartesianProductCalculator {
    @Override
    public <T> List<List<T>> calculate(Collection<? extends Collection<T>> vectors) {
        List<List<T>> result = new LinkedList<>();
        if (Objects.isNull(vectors) || vectors.isEmpty()) {
            return result;
        }

        // Start with an empty list
        result.add(new ArrayList<>());

        for (Collection<T> vector : vectors) {
            List<List<T>> temp = new LinkedList<>();
            for (List<T> combination : result) {
//                int j = 0;
                for (T element : vector) {
                    LinkedList<T> newCombination = new LinkedList<>(combination);
//                    int idx = Math.min(combination.size(), j);
//                    newCombination.add(idx, element);
                    newCombination.add(element);
                    temp.add(newCombination);
//                    j++;
                }
            }
            result = temp;
        }
        return result;
    }
}
