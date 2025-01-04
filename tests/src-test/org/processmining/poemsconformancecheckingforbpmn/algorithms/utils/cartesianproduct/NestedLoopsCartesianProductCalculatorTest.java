package org.processmining.poemsconformancecheckingforbpmn.algorithms.utils.cartesianproduct;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class NestedLoopsCartesianProductCalculatorTest {
    private final CartesianProductCalculator calculator;
    private List<List<Integer>> vectors;
    private List<List<Integer>> result;


    public NestedLoopsCartesianProductCalculatorTest() {
        this.calculator = new NestedLoopsCartesianProductCalculator();
    }
    @Before
    public void setUp() throws Exception {
        vectors = Arrays.asList(
                Arrays.asList(1, 2),
                Arrays.asList(3, 4),
                Arrays.asList(5, 6)
        );
        result = Arrays.asList(
                Arrays.asList(1, 3, 5),
                Arrays.asList(1, 3, 6),
                Arrays.asList(1, 4, 5),
                Arrays.asList(1, 4, 6),
                Arrays.asList(2, 3, 5),
                Arrays.asList(2, 3, 6),
                Arrays.asList(2, 4, 5),
                Arrays.asList(2, 4, 6)
        );
    }

    @Test
    public void calculate() {
        List<List<Integer>> cartesianProduct = calculator.calculate(vectors);
        assertEquals(result, cartesianProduct);
    }
}