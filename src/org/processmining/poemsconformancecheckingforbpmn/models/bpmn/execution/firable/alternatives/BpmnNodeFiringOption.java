package org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.firable.alternatives;

import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.marking.BpmnMarking;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.node.ExecutableBpmnNode;

public interface BpmnNodeFiringOption {
    ExecutableBpmnNode getNode();

    String getLabel();

    BpmnMarking getProducesMarking();

    BpmnMarking getConsumesMarking();

    String toString();
}
