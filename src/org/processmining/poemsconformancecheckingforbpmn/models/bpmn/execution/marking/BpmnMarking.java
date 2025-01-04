package org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.marking;

import org.processmining.models.graphbased.directed.bpmn.BPMNDiagram;
import org.processmining.models.graphbased.directed.bpmn.BPMNNode;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.marking.token.BpmnToken;
import org.processmining.poemsconformancecheckingforbpmn.models.execution.Marking;

import java.util.Set;

public interface BpmnMarking extends Marking<BpmnToken> {
    BPMNDiagram getModel();

    Set<BPMNNode> getReachableNodes();

    boolean equals(Object other);

    int hashCode();

    String toString();

    int nodeProducedTokensCount(BPMNNode sourceNode);

    int nodeConsumedTokensCount(BPMNNode sinkNode);
}
