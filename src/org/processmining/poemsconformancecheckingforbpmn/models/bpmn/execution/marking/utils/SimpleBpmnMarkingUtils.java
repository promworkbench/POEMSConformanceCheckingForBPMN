package org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.marking.utils;

import org.processmining.models.graphbased.directed.bpmn.BPMNDiagram;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.marking.BpmnMarking;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.marking.factory.BpmnMarkingFactory;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.marking.token.BpmnToken;
import org.processmining.poemsconformancecheckingforbpmn.models.utils.multiset.Multiset;
import org.processmining.poemsconformancecheckingforbpmn.models.utils.multiset.utils.MultisetUtils;

import java.util.Objects;

public class SimpleBpmnMarkingUtils implements BpmnMarkingUtils {
    private final MultisetUtils multisetUtils;
    private final BpmnMarkingFactory markingFactory;

    public SimpleBpmnMarkingUtils(MultisetUtils multisetUtils, BpmnMarkingFactory markingFactory) {
        this.multisetUtils = multisetUtils;
        this.markingFactory = markingFactory;
    }

    @Override
    public BpmnMarking union(BpmnMarking m1, BpmnMarking m2) {
        assert Objects.nonNull(m1);
        assert Objects.nonNull(m2);
        assert Objects.equals(m1.getModel(), m2.getModel());

        Multiset<BpmnToken> result = multisetUtils.union(m1, m2);

        return markingFactory.createWithoutCopy(m1.getModel(), result);
    }

    @Override
    public BpmnMarking sum(BpmnMarking m1, BpmnMarking m2) {
        assert Objects.nonNull(m1);
        assert Objects.nonNull(m2);
        assert Objects.equals(m1.getModel(), m2.getModel());

        Multiset<BpmnToken> result = multisetUtils.sum(m1, m2);

        return markingFactory.createWithoutCopy(m1.getModel(), result);
    }

    @Override
    public BpmnMarking difference(BpmnMarking m1, BpmnMarking m2) {
        assert Objects.nonNull(m1);
        assert Objects.nonNull(m2);
        assert Objects.equals(m1.getModel(), m2.getModel());

        Multiset<BpmnToken> result = multisetUtils.difference(m1, m2);

        return markingFactory.createWithoutCopy(m1.getModel(), result);
    }

    @Override
    public BpmnMarking intersection(BpmnMarking m1, BpmnMarking m2) {
        assert Objects.nonNull(m1);
        assert Objects.nonNull(m2);
        assert Objects.equals(m1.getModel(), m2.getModel());

        Multiset<BpmnToken> result = multisetUtils.intersection(m1, m2);

        return markingFactory.createWithoutCopy(m1.getModel(), result);
    }

    @Override
    public BpmnMarking multiply(BpmnMarking m1, int times) {
        assert Objects.nonNull(m1);
        return markingFactory.createWithoutCopy(m1.getModel(), multisetUtils.multiply(m1, times));
    }

    public BpmnMarking emptyMarking(BPMNDiagram model) {
        return markingFactory.getEmpty(model);
    }

    @Override
    public boolean isSubset(BpmnMarking superSet, BpmnMarking subSet) {
        return multisetUtils.isSubset(superSet, subSet);
    }

    @Override
    public int isContainedTimes(BpmnMarking superSet, BpmnMarking subSet) {
        return multisetUtils.isContainedTimes(superSet, subSet);
    }

    @Override
    public BpmnMarking copy(BpmnMarking marking) {
        return markingFactory.create(marking.getModel(), marking);
    }
}
