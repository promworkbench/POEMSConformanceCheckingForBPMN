package org.processmining.sccwbpmnnpos.models.bpmn.execution.marking;

import org.processmining.models.graphbased.directed.bpmn.BPMNDiagram;
import org.processmining.models.graphbased.directed.bpmn.BPMNNode;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.token.BpmnToken;
import org.processmining.sccwbpmnnpos.models.execution.Marking;
import org.processmining.sccwbpmnnpos.models.utils.multiset.Multiset;
import org.processmining.sccwbpmnnpos.models.utils.multiset.ReadOnlyMultiset;

import java.util.Set;

public interface BpmnMarking extends Marking<BpmnToken> {
    BPMNDiagram getModel();

    Set<BPMNNode> getReachableNodes();

    boolean equals(Object other);

    int hashCode();

    String toString();
}
