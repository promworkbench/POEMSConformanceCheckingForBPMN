package org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.utils;

import org.processmining.models.graphbased.directed.bpmn.BPMNDiagram;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.BpmnMarking;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.factory.BpmnMarkingFactory;
import org.processmining.sccwbpmnnpos.models.utils.multiset.utils.MultisetUtils;

public interface BpmnMarkingUtils {
    static BpmnMarkingUtils getInstance() {
        return new SimpleBpmnMarkingUtils(MultisetUtils.getInstance(), BpmnMarkingFactory.getInstance());
    }

    BpmnMarking union(BpmnMarking m1, BpmnMarking m2);

    BpmnMarking sum(BpmnMarking m1, BpmnMarking m2);

    BpmnMarking difference(BpmnMarking m1, BpmnMarking m2);

    BpmnMarking intersection(BpmnMarking m1, BpmnMarking m2);

    BpmnMarking multiply(BpmnMarking m1, int times);

    BpmnMarking emptyMarking(BPMNDiagram model);

    boolean isSubset(BpmnMarking superSet, BpmnMarking subSet);

    int isContainedTimes(BpmnMarking superSet, BpmnMarking subSet);
}
