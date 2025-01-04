package org.processmining.poemsconformancecheckingforbpmn.models.utils.ordered_set;

import org.junit.Assert;
import org.junit.Test;
import org.processmining.poemsconformancecheckingforbpmn.models.utils.ordered_set.exceptions.PartialOrderLoopNotAllowedException;
import org.processmining.poemsconformancecheckingforbpmn.models.utils.ordered_set.partial.ArrayPartiallyOrderedSet;
import org.processmining.poemsconformancecheckingforbpmn.models.utils.ordered_set.partial.PartiallyOrderedSet;

import java.util.HashSet;
import java.util.Set;

public class ArrayPartiallyOrderedSetTest {

    @Test
    public void testNormalNoError() throws PartialOrderLoopNotAllowedException {
        PartiallyOrderedSet<Integer> poSet = new ArrayPartiallyOrderedSet<>();
        poSet.add(5);
        poSet.add(3);
        poSet.add(7);
        poSet.add(2);
        poSet.add(6);

        poSet.setPredecessor(3, 5);
        poSet.setPredecessor(7, 5);
        poSet.setPredecessor(2, 3);
        poSet.setPredecessor(6, 2);
        poSet.setPredecessor(6, 7);


        Set<Integer> executed = new HashSet<>();
        Set<Integer> enabled = poSet.getEnabled(executed);
        Set<Integer> expectedEnabled = new HashSet<>();
        expectedEnabled.add(5);
        Assert.assertEquals(expectedEnabled, enabled);
        executed.addAll(enabled);
        enabled = poSet.getEnabled(executed);
        expectedEnabled.clear();
        expectedEnabled.add(3);
        expectedEnabled.add(7);
        Assert.assertEquals(expectedEnabled, enabled);
        executed.addAll(enabled);
        enabled = poSet.getEnabled(executed);
        expectedEnabled.clear();
        expectedEnabled.add(2);
        Assert.assertEquals(expectedEnabled, enabled);
        executed.addAll(enabled);
        enabled = poSet.getEnabled(executed);
        expectedEnabled.clear();
        expectedEnabled.add(6);
        Assert.assertEquals(expectedEnabled, enabled);
        executed.addAll(enabled);


        System.out.println(poSet);
    }

    @Test(expected = PartialOrderLoopNotAllowedException.class)
    public void loopError() throws PartialOrderLoopNotAllowedException {
        PartiallyOrderedSet<Integer> poSet = new ArrayPartiallyOrderedSet<>();
        poSet.add(1);
        poSet.add(2);
        poSet.add(3);

        poSet.setPredecessor(2, 1);
        poSet.setPredecessor(3, 2);
        poSet.setPredecessor(1, 3);
    }

    @Test(expected = PartialOrderLoopNotAllowedException.class)
    public void outOfOrderFakeLoop() throws PartialOrderLoopNotAllowedException {
        PartiallyOrderedSet<Integer> poSet = new ArrayPartiallyOrderedSet<>();
        poSet.add(1);
        poSet.add(2);
        poSet.add(3);

        poSet.setPredecessor(1, 3);
    }

    @Test()
    public void withoutAddingFirst() throws PartialOrderLoopNotAllowedException {
        PartiallyOrderedSet<Integer> poSet = new ArrayPartiallyOrderedSet<>();
        poSet.setPredecessor(2, 1);
        poSet.setPredecessor(3, 1);

        Set<Integer> executed = new HashSet<>();
        Set<Integer> enabled = poSet.getEnabled(executed);
        Set<Integer> expectedEnabled = new HashSet<>();
        expectedEnabled.add(1);
        Assert.assertEquals(expectedEnabled, enabled);
        executed.addAll(enabled);
        enabled = poSet.getEnabled(executed);
        expectedEnabled.clear();
        expectedEnabled.add(2);
        expectedEnabled.add(3);
        Assert.assertEquals(expectedEnabled, enabled);
    }
}