package org.processmining.sccwbpmnnpos.models.utils.ordered_set.partial.eventbased;

import org.junit.Test;
import org.processmining.sccwbpmnnpos.models.utils.ordered_set.exceptions.PartialOrderLoopNotAllowedException;

public class AbstractEventBasedPartiallyOrderedSetTest {

    @Test
    public void concatenate() throws PartialOrderLoopNotAllowedException {
        EventBasedPartiallyOrderedSet<Integer> po1 = new RepetitiveEventBasedPartiallyOrderedSet<>();
        EventBasedPartiallyOrderedSet.Event<Integer> e1 = po1.fire(1);
        EventBasedPartiallyOrderedSet.Event<Integer> e2 = po1.fire(2);
        EventBasedPartiallyOrderedSet.Event<Integer> e3 = po1.fire(3);
        EventBasedPartiallyOrderedSet.Event<Integer> e4 = po1.fire(4);
        EventBasedPartiallyOrderedSet.Event<Integer> e5 = po1.fire(5);
        EventBasedPartiallyOrderedSet.Event<Integer> e6 = po1.fire(6);
        po1.connect(e1, e2);
        po1.connect(e1, e3);
        po1.connect(e3, e4);
        po1.connect(e2, e5);
        po1.connect(e4, e6);
        po1.connect(e5, e6);
        System.out.println(po1);

        EventBasedPartiallyOrderedSet<Integer> po2 = new RepetitiveEventBasedPartiallyOrderedSet<>();
        e4 = po2.fire(4);
        e1 = po2.fire(1);
        e2 = po2.fire(2);
        po2.connect(e4, e1);
        po2.connect(e4, e2);
        e4 = po2.fire(4);
        po2.connect(e2, e4);
        e6 = po2.fire(6);
        e3 = po2.fire(3);
        e5 = po2.fire(5);
        po2.connect(e1, e6);
        po2.connect(e1, e3);
        po2.connect(e4, e5);
        po2.connect(e6, e5);
        System.out.println(po2);

        EventBasedPartiallyOrderedSet<Integer> concatenation = new RepetitiveEventBasedPartiallyOrderedSet<>();
        concatenation.concatenate(po1);
        concatenation.concatenate(po2);
        concatenation.connect(4, po1.getTimesFired(4), 4, concatenation.getTimesFired(4) - po2.getTimesFired(4) + 1);
        System.out.println(concatenation);
    }
}