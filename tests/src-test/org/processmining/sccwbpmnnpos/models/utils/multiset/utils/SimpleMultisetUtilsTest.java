package org.processmining.sccwbpmnnpos.models.utils.multiset.utils;

import org.junit.Before;
import org.junit.Test;
import org.processmining.sccwbpmnnpos.models.utils.multiset.Multiset;
import org.processmining.sccwbpmnnpos.models.utils.multiset.factory.DefaultMultisetFactory;
import org.processmining.sccwbpmnnpos.models.utils.multiset.factory.MultisetFactory;

import static org.junit.Assert.*;

public class SimpleMultisetUtilsTest {
    private Multiset<String> mSet1;
    private Multiset<String> mSet2;
    private Multiset<String> union;
    private Multiset<String> sum;
    private Multiset<String> difference;
    private Multiset<String> intersection;
    private MultisetUtils utils;

    public SimpleMultisetUtilsTest() {
        DefaultMultisetFactory multisetFactor = new DefaultMultisetFactory();
        utils = new SimpleMultisetUtils(multisetFactor);
        MultisetFactory multisetFactory = new DefaultMultisetFactory();
        mSet1 = multisetFactory.getDefault();
        mSet2 = multisetFactory.getDefault();
        union = multisetFactory.getDefault();
        sum = multisetFactory.getDefault();
        difference = multisetFactory.getDefault();
        intersection = multisetFactory.getDefault();

        mSet1.add("Acko", 2);
        mSet1.add("Viki", 3);
        mSet1.add("Matea", 5);

        mSet2.add("Acko", 4);
        mSet2.add("Viki", 3);
        mSet2.add("Ivana", 2);

        union.add("Acko", 4);
        union.add("Viki", 3);
        union.add("Matea", 5);
        union.add("Ivana", 2);

        sum.add("Acko", 6);
        sum.add("Viki", 6);
        sum.add("Matea", 5);
        sum.add("Ivana", 2);

        difference.add("Matea", 5);

        intersection.add("Acko", 2);
        intersection.add("Viki", 3);
    }

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void union() {
        System.out.println(utils.union(mSet1, mSet2));
        System.out.println(union);
        assertEquals(utils.union(mSet1, mSet2), union);
    }

    @Test
    public void difference() {
        assertEquals(utils.difference(mSet1, mSet2), difference);
    }

    @Test
    public void sum() {
        assertEquals(utils.sum(mSet1, mSet2), sum);
    }

    @Test
    public void intersection() {
        assertEquals(utils.intersection(mSet1, mSet2), intersection);
    }
}