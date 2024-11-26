package org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.statespace;

import org.processmining.models.graphbased.directed.bpmn.BPMNDiagram;
import org.processmining.models.graphbased.directed.transitionsystem.ReachabilityGraph;

public interface Bpmn2ReachabilityGraphConverter {
    ReachabilityGraph convert(BPMNDiagram bpmnDiagram) throws BpmnNoOptionToCompleteException, BpmnUnboundedException;
}
